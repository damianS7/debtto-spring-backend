package com.damian.paynext.group.expenses;

import com.damian.paynext.group.expenses.dto.GroupExpenseDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupExpenseDTOMapper {
    public static GroupExpenseDTO toDTO(GroupExpense groupExpense) {
        return new GroupExpenseDTO(
                groupExpense.getId(),
                groupExpense.getGroup().getId(),
                groupExpense.getPayer().getId(),
                groupExpense.getAmount(),
                groupExpense.getDescription()
        );
    }

    public static Set<GroupExpenseDTO> toDTO(Set<GroupExpense> groupExpenses) {
        return groupExpenses
                .stream()
                .map(
                        GroupExpenseDTOMapper::toDTO
                ).collect(Collectors.toSet());
    }
}
