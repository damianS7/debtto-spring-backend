package com.damian.paynext.group.repayments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupRepaymentRepository extends JpaRepository<GroupRepayment, Long> {
    Set<GroupRepayment> findByGroupId(Long groupId);
}

