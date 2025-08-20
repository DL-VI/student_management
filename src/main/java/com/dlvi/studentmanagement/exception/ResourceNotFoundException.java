package com.dlvi.studentmanagement.exception;

public class ResourceNotFoundException extends RuntimeException{

   public ResourceNotFoundException(String message) {
      super(message);
   }
}
