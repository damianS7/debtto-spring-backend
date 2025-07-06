package com.damian.paynext.group.dto;

import com.damian.paynext.group.member.GroupMemberDTO;

import java.util.Set;

public record GroupDTO(
        Long id,
        String name,
        String description,
        GroupCustomerOwnerDTO owner,
        Set<GroupMemberDTO> members
) {
}
