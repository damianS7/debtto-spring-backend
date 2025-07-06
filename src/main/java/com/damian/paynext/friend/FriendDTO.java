package com.damian.paynext.friend;

public record FriendDTO(
        Long id,
        Long customerId,
        String name,
        String avatarFilename
) {
}
