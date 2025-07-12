package com.damian.paynext.group.expense.member;

import com.damian.paynext.customer.Customer;
import com.damian.paynext.customer.CustomerRepository;
import com.damian.paynext.group.Group;
import com.damian.paynext.group.GroupRepository;
import com.damian.paynext.group.expenses.GroupExpense;
import com.damian.paynext.group.expenses.GroupExpenseRepository;
import com.damian.paynext.group.expenses.GroupExpenseService;
import com.damian.paynext.group.expenses.http.GroupExpenseCreateRequest;
import com.damian.paynext.group.member.GroupMember;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupExpenseServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupExpenseRepository groupExpenseRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private GroupExpenseService groupExpenseService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        customerRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    void setUpContext(Customer customer) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(customer);
    }

    @Test
    @DisplayName("Should get all group expenses")
    void shouldGetAllGroupExpenses() {
        // given
        Customer customer = new Customer(
                1L,
                "customer@test.com",
                passwordEncoder.encode("123456")
        );
        //        setUpContext(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas description");
        Set<GroupMember> groupMembers = Set.of(
                new GroupMember(customer, group)
        );
        group.setMembers(
                groupMembers
        );

        GroupExpense groupExpense = new GroupExpense(
                group,
                customer,
                BigDecimal.valueOf(100),
                "Restaurant at the Casino"
        );

        GroupExpense groupExpense2 = new GroupExpense(
                group,
                customer,
                BigDecimal.valueOf(1000),
                "Hotel"
        );

        Set<GroupExpense> groupExpenses = Set.of(
                groupExpense,
                groupExpense2
        );

        // when
        when(groupExpenseService.getGroupExpenses(group.getId())).thenReturn(groupExpenses);
        Set<GroupExpense> result = groupExpenseService.getGroupExpenses(group.getId());

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupExpenseRepository, times(1)).findByGroupId(group.getId());
    }

    @Test
    @DisplayName("Should get add group expense")
    void shouldAddGroupExpense() {
        // given
        Customer customer = new Customer(
                1L,
                "customer@test.com",
                passwordEncoder.encode("123456")
        );

        setUpContext(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas description");
        group.setId(1L);
        Set<GroupMember> groupMembers = Set.of(
                new GroupMember(customer, group)
        );
        group.setMembers(
                groupMembers
        );

        GroupExpenseCreateRequest groupExpenseCreateRequest = new GroupExpenseCreateRequest(
                BigDecimal.valueOf(1000),
                "Hotel"
        );

        // when
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupExpenseRepository.save(any(GroupExpense.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );
        GroupExpense result = groupExpenseService.addGroupExpense(group.getId(), groupExpenseCreateRequest);

        // then
        assertNotNull(result);
        assertEquals(groupExpenseCreateRequest.amount(), result.getAmount());
        assertEquals(groupExpenseCreateRequest.description(), result.getDescription());
        verify(groupExpenseRepository, times(1)).save(any(GroupExpense.class));
    }

    @Test
    @DisplayName("Should get delete group expense")
    void shouldDeleteGroupExpense() {
        // given
        Customer customer = new Customer(
                1L,
                "customer@test.com",
                passwordEncoder.encode("123456")
        );

        setUpContext(customer);

        Group group = new Group("Trip to Vegas", "Trip to Vegas description");
        group.setId(1L);
        Set<GroupMember> groupMembers = Set.of(
                new GroupMember(customer, group)
        );
        group.setMembers(
                groupMembers
        );
        group.setOwner(customer);

        GroupExpense expense = new GroupExpense(
                group,
                customer,
                BigDecimal.valueOf(1000),
                "Hotel"
        );

        expense.setId(1L);

        // when
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupExpenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        doNothing().when(groupExpenseRepository).deleteById(expense.getId());
        groupExpenseService.deleteGroupExpense(expense.getId());

        // then
        verify(groupExpenseRepository, times(1)).deleteById(expense.getId());
    }
}
