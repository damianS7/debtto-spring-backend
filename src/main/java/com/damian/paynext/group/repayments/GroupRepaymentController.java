package com.damian.paynext.group.repayments;

import com.damian.paynext.group.repayments.dto.GroupRepaymentDTO;
import com.damian.paynext.group.repayments.http.GroupRepaymentCreateRequest;
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
public class GroupRepaymentController {
    private final GroupRepaymentService groupRepaymentService;

    @Autowired
    public GroupRepaymentController(
            GroupRepaymentService groupRepaymentService
    ) {
        this.groupRepaymentService = groupRepaymentService;
    }

    // endpoint to fetch all repayments from a group
    @GetMapping("/groups/{id}/repayments")
    public ResponseEntity<?> getRepayments(
            @PathVariable @NotNull @Positive
            Long id
    ) {
        Set<GroupRepayment> expenses = groupRepaymentService.getGroupRepayments(id);
        Set<GroupRepaymentDTO> expensesDTO = GroupRepaymentDTOMapper.toDTO(expenses);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expensesDTO);
    }

    // endpoint to create group repayments
    @PostMapping("/groups/{id}/repayments")
    public ResponseEntity<?> addRepayment(
            @PathVariable @NotNull @Positive
            Long id,
            @Validated @RequestBody
            GroupRepaymentCreateRequest request
    ) {
        GroupRepayment groupExpense = groupRepaymentService.addGroupRepayment(id, request);
        GroupRepaymentDTO groupDTO = GroupRepaymentDTOMapper.toDTO(groupExpense);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupDTO);
    }

    // endpoint to delete group repayments
    @DeleteMapping("/repayments/{id}")
    public ResponseEntity<?> deleteRepayment(
            @PathVariable @NotNull @Positive
            Long id
    ) {
        groupRepaymentService.deleteGroupRepayment(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}

