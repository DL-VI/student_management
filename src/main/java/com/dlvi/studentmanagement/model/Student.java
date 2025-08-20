package com.dlvi.studentmanagement.model;

import com.dlvi.studentmanagement.dto.StudentRequestDto;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Student {
   private int id;
   private String name;
   private String email;
   private LocalDate birthDate;
   private String sex;

   public Student(StudentRequestDto dto) {
      this.name = dto.name();
      this.email = dto.email();
      this.birthDate = dto.birthDate();
      this.sex = dto.sex();
   }
}
