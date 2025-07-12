package com.damian.paynext.group.members;

import com.damian.paynext.common.exception.Exceptions;
import com.damian.paynext.common.utils.AuthHelper;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.customer.CustomerRepository;
import com.damian.paynext.customer.exception.CustomerNotFoundException;
import com.damian.paynext.group.group.Group;
import com.damian.paynext.group.group.GroupRepository;
import com.damian.paynext.group.group.exception.GroupAuthorizationException;
import com.damian.paynext.group.group.exception.GroupNotFoundException;
import com.damian.paynext.group.members.exception.GroupMemberNotFoundException;
import com.damian.paynext.group.members.http.GroupMemberUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final CustomerRepository customerRepository;

    public GroupMemberService(
            GroupMemberRepository groupMemberRepository,
            GroupRepository groupRepository,
            CustomerRepository customerRepository
    ) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
        this.customerRepository = customerRepository;
    }

    public Set<GroupMember> getGroupMembers(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }

    public GroupMember addGroupMember(Long groupId, GroupMemberUpdateRequest request) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check authorization
        if (!loggedCustomer.getId().equals(group.getOwner().getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.ACCESS_FORBIDDEN);
        }

        Customer customerToAdd = customerRepository.findById(request.memberId()).orElseThrow(
                () -> new CustomerNotFoundException(Exceptions.CUSTOMER.NOT_FOUND)
        );

        GroupMember groupMember = new GroupMember(
                customerToAdd,
                group
        );

        return groupMemberRepository.save(groupMember);
    }

    public void removeGroupMember(Long groupMemberId) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        GroupMember groupMember = groupMemberRepository.findById(groupMemberId).orElseThrow(
                () -> new GroupMemberNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        Group group = groupRepository.findById(groupMember.getGroup().getId()).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check authorization
        if (!loggedCustomer.getId().equals(group.getOwner().getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.ACCESS_FORBIDDEN);
        }

        groupMemberRepository.deleteById(groupMemberId);
    }
}
