package com.damian.paynext.group.expenses;

import com.damian.paynext.auth.http.AuthenticationRequest;
import com.damian.paynext.auth.http.AuthenticationResponse;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.customer.CustomerGender;
import com.damian.paynext.customer.CustomerRepository;
import com.damian.paynext.customer.CustomerRole;
import com.damian.paynext.group.expenses.dto.GroupExpenseDTO;
import com.damian.paynext.group.expenses.http.GroupExpenseCreateRequest;
import com.damian.paynext.group.group.Group;
import com.damian.paynext.group.group.GroupRepository;
import com.damian.paynext.group.members.GroupMember;
import com.damian.paynext.group.members.GroupMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupExpenseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GroupExpenseRepository groupExpenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Customer customer;
    private String token;

    @BeforeEach
    void setUp() {
        groupExpenseRepository.deleteAll();
        groupMemberRepository.deleteAll();
        groupRepository.deleteAll();
        customerRepository.deleteAll();

        customer = new Customer();
        customer.setRole(CustomerRole.CUSTOMER);
        customer.setEmail("customer@test.com");
        customer.setPassword(bCryptPasswordEncoder.encode("123456"));

        customer.getProfile().setFirstName("John");
        customer.getProfile().setLastName("Wick");
        customer.getProfile().setGender(CustomerGender.MALE);
        customer.getProfile().setBirthdate(LocalDate.of(1989, 1, 1));

        customerRepository.save(customer);
    }

    void loginWithCustomer(Customer customer) throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                customer.getEmail(), "123456"
        );

        String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);

        // when
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(jsonRequest))
                                  .andReturn();

        AuthenticationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthenticationResponse.class
        );

        token = response.token();
    }

    @Test
    @DisplayName("Should get group expenses")
    void shouldGetGroupExpenses() throws Exception {
        // given
        loginWithCustomer(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas 2025");
        group.setOwner(customer);
        groupRepository.save(group);

        groupMemberRepository.save(
                new GroupMember(customer, group)
        );

        groupExpenseRepository.save(
                new GroupExpense(group, customer, BigDecimal.valueOf(1000), "Trip to Vegas")
        );

        // when
        MvcResult result = mockMvc
                .perform(
                        get("/api/v1/groups/{id}/expenses", group.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        GroupExpenseDTO[] expensesDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                GroupExpenseDTO[].class
        );

        // then
        assertThat(expensesDTO).isNotNull();
        assertThat(expensesDTO.length).isEqualTo(1);
    }


    @Test
    @DisplayName("Should add group expenses")
    void shouldAddGroupExpense() throws Exception {
        // given
        loginWithCustomer(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas 2025");
        group.setOwner(customer);
        groupRepository.save(group);

        groupMemberRepository.save(
                new GroupMember(customer, group)
        );

        GroupExpenseCreateRequest request = new GroupExpenseCreateRequest(
                BigDecimal.valueOf(1000),
                "Hotel at the Casino"
        );

        // when
        MvcResult result = mockMvc
                .perform(
                        post("/api/v1/groups/{id}/expenses", group.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        GroupExpenseDTO expenseDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                GroupExpenseDTO.class
        );

        // then
        assertThat(expenseDTO).isNotNull();
    }

    @Test
    @DisplayName("Should delete group expenses")
    void shouldDeleteGroupExpense() throws Exception {
        // given
        loginWithCustomer(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas 2025");
        group.setOwner(customer);
        groupRepository.save(group);

        groupMemberRepository.save(
                new GroupMember(customer, group)
        );

        GroupExpense groupExpense = new GroupExpense(
                group, customer, BigDecimal.valueOf(1000), "Hotel"
        );
        groupExpenseRepository.save(groupExpense);

        // when
        mockMvc.perform(
                       delete("/api/v1/expenses/{id}", groupExpense.getId())
                               .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
               )
               .andDo(print())
               .andExpect(MockMvcResultMatchers.status().is(204))
               .andReturn();
    }

}
