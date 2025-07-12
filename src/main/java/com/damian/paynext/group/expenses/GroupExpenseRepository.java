package com.damian.paynext.group.expenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupExpenseRepository extends JpaRepository<GroupExpense, Long> {
    Set<GroupExpense> findByGroupId(Long groupId);
}

