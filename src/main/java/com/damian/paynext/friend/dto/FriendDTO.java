package com.damian.paynext.friend.dto;

public record FriendDTO(
        Long id,
        Long customerId,
        String name,
        String avatarFilename
) {
}
