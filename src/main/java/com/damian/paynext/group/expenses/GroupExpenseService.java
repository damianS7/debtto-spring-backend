package com.damian.paynext.group.expenses;

import com.damian.paynext.common.exception.Exceptions;
import com.damian.paynext.common.utils.AuthHelper;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.group.expenses.exception.GroupExpenseNotFoundException;
import com.damian.paynext.group.expenses.http.GroupExpenseCreateRequest;
import com.damian.paynext.group.group.Group;
import com.damian.paynext.group.group.GroupRepository;
import com.damian.paynext.group.group.exception.GroupAuthorizationException;
import com.damian.paynext.group.group.exception.GroupNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GroupExpenseService {
    private final GroupExpenseRepository groupExpenseRepository;
    private final GroupRepository groupRepository;

    public GroupExpenseService(
            GroupExpenseRepository groupExpenseRepository,
            GroupRepository groupRepository
    ) {
        this.groupExpenseRepository = groupExpenseRepository;
        this.groupRepository = groupRepository;
    }

    public Set<GroupExpense> getGroupExpenses(Long groupId) {
        return groupExpenseRepository.findByGroupId(groupId);
    }

    public GroupExpense addGroupExpense(Long groupId, GroupExpenseCreateRequest request) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check if customer belongs to the group
        if (group.getMembers().stream().noneMatch(
                member -> member.getMember().getId().equals(loggedCustomer.getId())
        )) {

            throw new GroupAuthorizationException(Exceptions.GROUP.NOT_MEMBER);
        }

        GroupExpense expense = new GroupExpense(
                group,
                loggedCustomer
        );
        expense.setDescription(request.description());
        expense.setAmount(request.amount());

        return groupExpenseRepository.save(expense);
    }

    public void deleteGroupExpense(Long expenseId) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        GroupExpense expense = groupExpenseRepository.findById(expenseId).orElseThrow(
                () -> new GroupExpenseNotFoundException(Exceptions.EXPENSES.NOT_FOUND)
        );

        Group group = groupRepository.findById(expense.getGroup().getId()).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check customer is the owner of the group to delete the expenses.
        if (!loggedCustomer.getId().equals(group.getOwner().getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.NOT_OWNER);
        }

        groupExpenseRepository.deleteById(expenseId);
    }
}
