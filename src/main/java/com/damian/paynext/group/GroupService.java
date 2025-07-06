package com.damian.paynext.group;

import com.damian.paynext.common.exception.Exceptions;
import com.damian.paynext.common.utils.AuthHelper;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.customer.CustomerRepository;
import com.damian.paynext.group.exception.GroupAuthorizationException;
import com.damian.paynext.group.exception.GroupNotFoundException;
import com.damian.paynext.group.http.GroupCreateRequest;
import com.damian.paynext.group.http.GroupUpdateRequest;
import com.damian.paynext.group.member.GroupMember;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class GroupService {
    private final CustomerRepository customerRepository;
    private final GroupRepository groupRepository;

    public GroupService(
            CustomerRepository customerRepository,
            GroupRepository groupRepository
    ) {
        this.customerRepository = customerRepository;
        this.groupRepository = groupRepository;
    }

    public Set<Group> getGroups() {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();
        return groupRepository.findBelongingGroupsByCustomerId(loggedCustomer.getId());
    }

    public Group getGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );
    }

    public Group createGroup(GroupCreateRequest request) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();
        Group group = new Group(
                request.name(),
                request.description()
        );
        group.setOwner(loggedCustomer);

        // add the logged customer as member
        GroupMember groupMember = new GroupMember(
                loggedCustomer,
                group
        );
        group.addMember(groupMember);

        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, GroupUpdateRequest request) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check if the logged customer is the owner of the group.
        if (!loggedCustomer.getId().equals(group.getOwner().getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.ACCESS_FORBIDDEN);
        }

        group.setName(request.name());
        group.setDescription(request.description());

        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        // check if the group exists
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new GroupNotFoundException(Exceptions.GROUP.NOT_FOUND)
        );

        // check if the customer is the owner of the group
        if (!group.getOwner().getId().equals(loggedCustomer.getId())) {
            throw new GroupAuthorizationException(Exceptions.GROUP.ACCESS_FORBIDDEN);
        }

        groupRepository.deleteById(id);
    }
}
