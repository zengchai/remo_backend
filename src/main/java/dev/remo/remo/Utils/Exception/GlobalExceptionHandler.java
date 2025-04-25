package dev.remo.remo.Utils.Exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.remo.remo.Models.Response.GeneralResponse;
import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OwnershipNotMatchException.class)
    public ResponseEntity<?> handleOwnershipNotMatch(OwnershipNotMatchException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<?> handleInvalidStatus(InvalidStatusException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<?> handleNotFound(NotFoundResourceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleFileUploadException(InternalServerErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(
                        GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleJwtExceptions(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleGeneric(InternalAuthenticationServiceException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                GeneralResponse.builder().success(false).error("Invalid email or password").build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GeneralResponse.builder().success(false).error("An unexpected error occurred").build());
    }
}
