package com.damian.paynext.friend;

import com.damian.paynext.friend.dto.FriendDTO;
import com.damian.paynext.friend.http.FriendCreateRequest;
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
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // endpoint to fetch all friends from logged customer
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {
        Set<Friend> friends = friendService.getFriends();
        Set<FriendDTO> friendsDTO = FriendDTOMapper.toFriendDTOList(friends);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(friendsDTO);
    }

    // endpoint to add a new friend for the logged customer
    @PostMapping("/friends")
    public ResponseEntity<?> addFriend(
            @Validated @RequestBody
            FriendCreateRequest request
    ) {
        Friend friend = friendService.addFriend(request.customerId());
        FriendDTO friendDTO = FriendDTOMapper.toCustomerFriendDTO(friend);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(friendDTO);
    }

    // endpoint to delete a friend from the logged customer friend list.
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<?> deleteFriend(
            @PathVariable @NotNull @Positive
            Long id
    ) {
        friendService.deleteFriend(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}

