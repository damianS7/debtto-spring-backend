package com.damian.paynext.group.repayments;

import com.damian.paynext.group.repayments.dto.GroupRepaymentDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupRepaymentDTOMapper {
    public static GroupRepaymentDTO toDTO(GroupRepayment groupRepayment) {
        return new GroupRepaymentDTO(
                groupRepayment.getId(),
                groupRepayment.getGroup().getId(),
                groupRepayment.getPayer().getId(),
                groupRepayment.getAmount()
        );
    }

    public static Set<GroupRepaymentDTO> toDTO(Set<GroupRepayment> groupRepayments) {
        return groupRepayments
                .stream()
                .map(
                        GroupRepaymentDTOMapper::toDTO
                ).collect(Collectors.toSet());
    }
}
