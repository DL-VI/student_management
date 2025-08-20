package com.dlvi.studentmanagement.exception;

import com.dlvi.studentmanagement.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();

      ex.getBindingResult().getFieldErrors().forEach(error ->
         errors.put(error.getField(), error.getDefaultMessage())
      );

      ApiResponse<Map<String, String>> response = new ApiResponse<>(
         false,
         "Validation failed",
         errors
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
   }

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request));
   }

   @ExceptionHandler(DatabaseOperationException.class)
   public ResponseEntity<ErrorResponse> handleDatabaseOperationError(DatabaseOperationException ex, HttpServletRequest request) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request));
   }

   @ExceptionHandler(EmailAlreadyExistsException.class)
   public ResponseEntity<ErrorResponse> handleValidation(EmailAlreadyExistsException ex, HttpServletRequest request) {
      return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request));
   }

   private ErrorResponse buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
      return new ErrorResponse(
         status.value(),
         status.getReasonPhrase(),
         ex.getMessage(),
         request.getRequestURI());
   }
}
