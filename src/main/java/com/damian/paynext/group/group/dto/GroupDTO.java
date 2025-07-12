package com.damian.paynext.group.group.dto;

import com.damian.paynext.group.members.dto.GroupMemberDTO;

import java.util.Set;

public record GroupDTO(
        Long id,
        String name,
        String description,
        GroupCustomerOwnerDTO owner,
        Set<GroupMemberDTO> members
) {
}
