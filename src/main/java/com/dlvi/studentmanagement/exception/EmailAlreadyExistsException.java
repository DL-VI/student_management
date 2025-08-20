package com.dlvi.studentmanagement.exception;

public class EmailAlreadyExistsException extends RuntimeException {
   public EmailAlreadyExistsException(String message) {
      super(message);
   }
}
