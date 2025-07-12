package com.damian.paynext.common.exception;

import com.damian.paynext.auth.exception.*;
import com.damian.paynext.common.utils.ApiResponse;
import com.damian.paynext.customer.exception.CustomerEmailTakenException;
import com.damian.paynext.customer.exception.CustomerException;
import com.damian.paynext.customer.exception.CustomerNotFoundException;
import com.damian.paynext.customer.profile.exception.ProfileAuthorizationException;
import com.damian.paynext.customer.profile.exception.ProfileException;
import com.damian.paynext.customer.profile.exception.ProfileNotFoundException;
import com.damian.paynext.friend.exception.FriendAlreadyExistException;
import com.damian.paynext.friend.exception.FriendAuthorizationException;
import com.damian.paynext.friend.exception.FriendNotFoundException;
import com.damian.paynext.friend.exception.MaxFriendsLimitReachedException;
import com.damian.paynext.group.group.exception.GroupAuthorizationException;
import com.damian.paynext.group.group.exception.GroupNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation error", errors, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(
            {
                    AuthenticationException.class,
                    JwtAuthenticationException.class,
                    AuthenticationBadCredentialsException.class,
                    AccountDisabledException.class
            }
    )
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(
            {
                    EntityNotFoundException.class,
                    CustomerNotFoundException.class,
                    ProfileNotFoundException.class,
                    FriendNotFoundException.class,
                    GroupNotFoundException.class
            }
    )
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(
            {
                    CustomerEmailTakenException.class,
                    MaxFriendsLimitReachedException.class,
                    MaxUploadSizeExceededException.class,
                    FriendAlreadyExistException.class

            }
    )
    public ResponseEntity<ApiResponse<String>> handleConflitException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(
            {
                    ApplicationException.class,
                    ProfileException.class,
                    CustomerException.class,
            }
    )
    public ResponseEntity<ApiResponse<String>> handleApplicationException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(
            {
                    RuntimeException.class,
                    Exception.class
            }
    )
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(
            {
                    AuthorizationException.class,
                    FriendAuthorizationException.class,
                    GroupAuthorizationException.class,
                    ProfileAuthorizationException.class,
                    PasswordMismatchException.class
            }
    )
    public ResponseEntity<ApiResponse<String>> handleAuthorizationException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(ApiResponse.error(ex.getMessage(), HttpStatus.FORBIDDEN));
    }
}