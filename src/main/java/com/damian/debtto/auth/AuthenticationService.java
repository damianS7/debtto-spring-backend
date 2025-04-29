package com.damian.debtto.auth;

import com.damian.debtto.auth.exception.AuthenticationAccountDisabledException;
import com.damian.debtto.auth.exception.AuthenticationBadCredentialsException;
import com.damian.debtto.auth.http.AuthenticationRequest;
import com.damian.debtto.auth.http.AuthenticationResponse;
import com.damian.debtto.common.exception.PasswordMismatchException;
import com.damian.debtto.common.utils.JWTUtil;
import com.damian.debtto.customer.Customer;
import com.damian.debtto.customer.CustomerAccountStatus;
import com.damian.debtto.customer.CustomerRepository;
import com.damian.debtto.customer.CustomerService;
import com.damian.debtto.customer.exception.CustomerNotFoundException;
import com.damian.debtto.customer.http.request.CustomerPasswordUpdateRequest;
import com.damian.debtto.customer.http.request.CustomerRegistrationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthenticationService {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerRepository customerRepository;

    public AuthenticationService(
            JWTUtil jwtUtil,
            AuthenticationManager authenticationManager,
            CustomerService customerService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            CustomerRepository customerRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerRepository = customerRepository;
    }

    /**
     * Register a new customer.
     *
     * @param request Contains the fields needed for the customer creation
     * @return The customer created
     */
    public Customer register(CustomerRegistrationRequest request) {
        // It uses the customer service to create a new customer
        Customer registeredCustomer = customerService.createCustomer(request);

        // send welcome email
        // Generate token for email validation
        // send email to confirm registration

        return registeredCustomer;
    }

    /**
     * Controls the login flow.
     *
     * @param request Contains the fields needed to login into the service
     * @return Contains the data (Customer, Profile) and the token
     * @throws AuthenticationBadCredentialsException  if credentials are invalid
     * @throws AuthenticationAccountDisabledException if the account is not enabled
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        final String email = request.email();
        final String password = request.password();
        final Authentication auth;

        try {
            // Authenticate the user
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, password)
            );
        } catch (BadCredentialsException e) {
            throw new AuthenticationBadCredentialsException();
        }

        // Generate a token for the authenticated user
        final String token = jwtUtil.generateToken(email);

        // Get the authenticated user
        final Customer customer = (Customer) auth.getPrincipal();

        // check if the account is disabled
        if (customer.getAccountStatus().equals(CustomerAccountStatus.DISABLED)) {
            throw new AuthenticationAccountDisabledException("Account is disabled.");
        }

        // Return the customer data and the token
        return new AuthenticationResponse(
                customer.toDTO(), token
        );
    }

    /**
     * It updates the password of a customer
     *
     * @param request the request body that contains the current password and the new password
     * @return the customer updated
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws PasswordMismatchException if the password does not match
     */
    public void updatePassword(CustomerPasswordUpdateRequest request) {
        // we extract the email from the Customer stored in the SecurityContext
        final Customer loggedCustomer = (Customer) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // we get the Customer entity so we can save at the end
        Customer customer = customerRepository.findById(loggedCustomer.getId()).orElseThrow(
                () -> new CustomerNotFoundException(loggedCustomer.getId())
        );

        // Before making any changes we check that the password sent by the customer matches the one in the entity
        if (!bCryptPasswordEncoder.matches(request.currentPassword(), customer.getPassword())) {
            throw new PasswordMismatchException("Password does not match.");
        }

        // if a new password is specified we set in the customer entity
        if (request.newPassword() != null) {
            customer.setPassword(
                    bCryptPasswordEncoder.encode(request.newPassword())
            );
        }

        // we change the updateAt timestamp field
        customer.setUpdatedAt(Instant.now());

        // save the changes
        customerRepository.save(customer);
    }
}
