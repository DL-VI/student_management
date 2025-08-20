package com.dlvi.studentmanagement.model;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class ApiResponse<T> {
   private LocalDateTime timestamp;
   private boolean success;
   private String message;
   private T data;

   public ApiResponse(boolean success, String message, T data) {
      this.timestamp = LocalDateTime.now();
      this.success = success;
      this.message = message;
      this.data = data;
   }
}
