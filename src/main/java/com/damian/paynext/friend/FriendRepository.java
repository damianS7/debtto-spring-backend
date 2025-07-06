package com.damian.paynext.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Set<Friend> findAllByCustomerId(Long customerId);

    @Query("SELECT COUNT(c) > 0 FROM Friend c WHERE c.customer.id = :customerId AND c.friend.id = :friendCustomerId")
    boolean friendExists(@Param("customerId") Long customerId, @Param("friendCustomerId") Long friendCustomerId);
}

