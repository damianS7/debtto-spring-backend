package com.damian.paynext.group.repayments;

import com.damian.paynext.common.exception.Exceptions;
import com.damian.paynext.common.utils.AuthHelper;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.group.group.Group;
import com.damian.paynext.group.group.GroupRepository;
import com.damian.paynext.group.group.exception.GroupAuthorizationException;
import com.damian.paynext.group.group.exception.GroupNotFoundException;
import com.damian.paynext.group.repayments.exception.GroupRepaymentNotFoundException;
import com.damian.paynext.group.repayments.http.GroupRepaymentCreateRequest;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GroupRepaymentService {
    private final GroupRepaymentRepository groupRepaymentRepository;
    private final GroupRepository groupRepository;

    public GroupRepaymentService(
            GroupRepaymentRepository groupRepaymentRepository,
            GroupRepository groupRepository
    ) {
        this.groupRepaymentRepository = groupRepaymentRepository;
        this.groupRepository = groupRepository;
    }

    public Set<GroupRepayment> getGroupRepayments(Long groupId) {
        return groupRepaymentRepository.findByGroupId(groupId);
    }

    public GroupRepayment addGroupRepayment(Long groupId, GroupRepaymentCreateRequest request) {
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

        GroupRepayment expense = new GroupRepayment(
                group,
                loggedCustomer,
                request.amount()
        );

        return groupRepaymentRepository.save(expense);
    }

    public void deleteGroupRepayment(Long expenseId) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        GroupRepayment expense = groupRepaymentRepository.findById(expenseId).orElseThrow(
                () -> new GroupRepaymentNotFoundException(Exceptions.EXPENSES.NOT_FOUND)
        );

        Group group = groupRepository.findById(expense.getGroup().getId()).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check customer is the owner of the group to delete the expenses.
        if (!loggedCustomer.getId().equals(group.getOwner().getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.NOT_OWNER);
        }

        groupRepaymentRepository.deleteById(expenseId);
    }
}
