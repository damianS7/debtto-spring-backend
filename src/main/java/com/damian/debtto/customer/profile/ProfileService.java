package com.damian.debtto.customer.profile;

import com.damian.debtto.auth.exception.AuthorizationException;
import com.damian.debtto.common.exception.PasswordMismatchException;
import com.damian.debtto.customer.Customer;
import com.damian.debtto.customer.CustomerGender;
import com.damian.debtto.customer.CustomerRole;
import com.damian.debtto.customer.profile.exception.ProfileException;
import com.damian.debtto.customer.profile.exception.ProfileNotFoundException;
import com.damian.debtto.customer.profile.http.request.ProfilePatchRequest;
import com.damian.debtto.customer.profile.http.request.ProfileUpdateRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ProfileService(ProfileRepository profileRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.profileRepository = profileRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * It updates the whole customer profile
     *
     * @param profileId the id of the profile to update
     * @param request   the fields to change
     * @return the profile updated
     */
    public Profile updateProfile(Long profileId, ProfileUpdateRequest request) {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", request.name());
        fieldsToUpdate.put("surname", request.surname());
        fieldsToUpdate.put("phone", request.phone());
        fieldsToUpdate.put("address", request.address());
        fieldsToUpdate.put("country", request.country());
        fieldsToUpdate.put("postalCode", request.postalCode());
        fieldsToUpdate.put("photoPath", request.photoPath());
        fieldsToUpdate.put("nationalId", request.nationalId());
        fieldsToUpdate.put("gender", request.gender().toString());
        fieldsToUpdate.put("birthdate", request.birthdate().toString());

        ProfilePatchRequest patchRequest = new ProfilePatchRequest(
                request.currentPassword(),
                fieldsToUpdate
        );

        return patchProfile(profileId, patchRequest);
    }

    public Profile patchCustomerProfile(ProfilePatchRequest request) {
        return this.patchProfile(null, request);
    }

    public Profile patchProfile(Long profileId, ProfilePatchRequest request) {
        // We get the customer logged in the context
        Customer customerLogged = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // if profiledId is null, then is a customer modifying its own profile.
        if (profileId == null) {
            profileId = Optional.ofNullable(customerLogged.getProfile())
                                .map(Profile::getId)
                                .orElseThrow(() -> new ProfileException("Cannot access to customer profile.")
                                );
        }

        // We get the profile we want to modify
        Profile profile = profileRepository.findById(profileId)
                                           .orElseThrow(
                                                   ProfileNotFoundException::new
                                           );

        // if the logged user is not admin
        if (!customerLogged.getRole().equals(CustomerRole.ADMIN)) {
            // before making any changes we check that the user sent the current password.
            if (!bCryptPasswordEncoder.matches(request.currentPassword(), customerLogged.getPassword())) {
                throw new PasswordMismatchException();
            }

            // we make sure that this profile belongs to the customer logged
            if (!profile.getCustomerId().equals(customerLogged.getId())) {
                throw new AuthorizationException("You are not the owner of this profile.");
            }
        }

        // we iterate over the fields (if any)
        request.fieldsToUpdate().forEach((key, value) -> {
            switch (key) {
                case "name" -> profile.setName((String) value);
                case "surname" -> profile.setSurname((String) value);
                case "phone" -> profile.setPhone((String) value);
                case "address" -> profile.setAddress((String) value);
                case "country" -> profile.setCountry((String) value);
                case "postalCode" -> profile.setPostalCode((String) value);
                case "photoPath" -> profile.setPhotoPath((String) value);
                case "nationalId" -> profile.setNationalId((String) value);
                case "gender" -> profile.setGender(CustomerGender.valueOf((String) value));
                case "birthdate" -> profile.setBirthdate(LocalDate.parse((String) value));
                default -> throw new ProfileException("Field '" + key + "' is not updatable.");
            }
        });

        // we change the updateAt timestamp field
        profile.setUpdatedAt(Instant.now());

        return profileRepository.save(profile);
    }
}
