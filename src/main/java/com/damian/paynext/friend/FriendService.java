package com.damian.paynext.friend;

import com.damian.paynext.common.exception.Exceptions;
import com.damian.paynext.common.utils.AuthHelper;
import com.damian.paynext.friend.exception.FriendAlreadyExistException;
import com.damian.paynext.friend.exception.FriendAuthorizationException;
import com.damian.paynext.friend.exception.FriendNotFoundException;
import com.damian.paynext.friend.exception.MaxFriendsLimitReachedException;
import com.damian.paynext.customer.Customer;
import com.damian.paynext.customer.CustomerRepository;
import com.damian.paynext.customer.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class FriendService {
    private final short MAX_FRIENDS = 10;
    private final FriendRepository friendRepository;
    private final CustomerRepository customerRepository;

    public FriendService(
            FriendRepository friendRepository,
            CustomerRepository customerRepository
    ) {
        this.friendRepository = friendRepository;
        this.customerRepository = customerRepository;
    }

    // get all the friends for the logged customer
    public Set<Friend> getFriends() {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();
        return friendRepository.findAllByCustomerId(loggedCustomer.getId());
    }

    // add a new friend for the logged customer
    public Friend addFriend(Long customerId) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        // check friend list size limit
        if (this.getFriends().size() >= MAX_FRIENDS) {
            throw new MaxFriendsLimitReachedException(Exceptions.FRIEND_LIST.MAX_FRIENDS);
        }

        // check if the customer we want to add as a friend exists.
        Customer friendCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException(Exceptions.CUSTOMER.NOT_FOUND)
        );

        // check if that they are not already friend
        if (friendRepository.friendExists(loggedCustomer.getId(), friendCustomer.getId())) {
            throw new FriendAlreadyExistException(Exceptions.FRIEND_LIST.ALREADY_EXISTS);
        }

        return friendRepository.save(
                new Friend(loggedCustomer, friendCustomer)
        );
    }

    // delete a friend from the friend list of the logged customer.
    public void deleteFriend(Long id) {
        Customer loggedCustomer = AuthHelper.getLoggedCustomer();

        // check if the friend exists
        Friend friend = friendRepository.findById(id).orElseThrow(
                () -> new FriendNotFoundException(Exceptions.FRIEND_LIST.NOT_FOUND)
        );

        // check if the logged customer is the owner of the friend.
        if (!loggedCustomer.getId().equals(friend.getCustomer().getId())) {
            throw new FriendAuthorizationException(Exceptions.FRIEND_LIST.ACCESS_FORBIDDEN);
        }

        friendRepository.deleteById(id);
    }
}
