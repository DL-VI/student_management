package com.dlvi.studentmanagement.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ErrorResponse {
   private LocalDateTime timestamp;
   private int status;
   private String error;
   private String message;
   private String path;

   public ErrorResponse(int status, String error, String message, String path) {
      this.timestamp = LocalDateTime.now();
      this.status = status;
      this.error = error;
      this.message = message;
      this.path = path;
   }
}
