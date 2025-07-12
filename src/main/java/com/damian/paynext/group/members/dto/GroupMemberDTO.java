package com.damian.paynext.group.members.dto;

public record GroupMemberDTO(
        Long id,
        Long groupId,
        Long customerId,
        String customerName,
        String customerAvatarFilename
) {
}
