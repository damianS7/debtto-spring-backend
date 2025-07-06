package com.damian.paynext.group.member;

public record GroupMemberDTO(
        Long id,
        Long groupId,
        Long customerId,
        String customerName,
        String customerAvatarFilename
) {
}
