package com.damian.paynext.group.expenses;

import com.damian.paynext.group.expenses.dto.GroupExpenseDTO;
import com.damian.paynext.group.expenses.http.GroupExpenseCreateRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/v1")
@RestController
public class GroupExpenseController {
    private final GroupExpenseService groupExpenseService;

    @Autowired
    public GroupExpenseController(GroupExpenseService groupExpenseService) {
        this.groupExpenseService = groupExpenseService;
    }

    // endpoint to fetch all expenses from a group
    @GetMapping("/groups/{id}/expenses")
    public ResponseEntity<?> getExpenses(
            @PathVariable @NotNull @Positive
            Long id
    ) {
        Set<GroupExpense> expenses = groupExpenseService.getGroupExpenses(id);
        Set<GroupExpenseDTO> expensesDTO = GroupExpenseDTOMapper.toDTO(expenses);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expensesDTO);
    }

    // endpoint to create group expenses
    @PostMapping("/groups/{id}/expenses")
    public ResponseEntity<?> addExpense(
            @PathVariable @NotNull @Positive
            Long id,
            @Validated @RequestBody
            GroupExpenseCreateRequest request
    ) {
        GroupExpense groupExpense = groupExpenseService.addGroupExpense(id, request);
        GroupExpenseDTO groupDTO = GroupExpenseDTOMapper.toDTO(groupExpense);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupDTO);
    }

    // endpoint to delete group expenses
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable @NotNull @Positive
            Long id
    ) {
        groupExpenseService.deleteGroupExpense(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}

