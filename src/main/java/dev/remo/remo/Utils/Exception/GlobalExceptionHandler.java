package dev.remo.remo.Utils.Exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.remo.remo.Models.Response.GeneralResponse;

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(MultipartfileUploadException.class)
    public ResponseEntity<?> handleFileUploadException(MultipartfileUploadException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        GeneralResponse.builder().success(false).error(ex.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GeneralResponse.builder().success(false).error("An unexpected error occurred").build());
    }
}
