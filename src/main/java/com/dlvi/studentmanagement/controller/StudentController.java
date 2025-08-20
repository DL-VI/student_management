package com.dlvi.studentmanagement.controller;

import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.dto.StudentRequestDto;
import com.dlvi.studentmanagement.dto.StudentResponseDto;
import com.dlvi.studentmanagement.model.ApiResponse;
import com.dlvi.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/student")
public class StudentController {
   private final StudentService studentService;

   public StudentController(StudentService studentService) {
      this.studentService = studentService;
   }

   @Transactional
   @PostMapping
   public ResponseEntity<ApiResponse<StudentResponseDto>> registerStudent(@Valid @RequestBody StudentRequestDto req) {
      StudentResponseDto responseDto = studentService.registerStudent(req);

      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
         true,
         "successful registration",
         responseDto));
   }

   @Transactional
   @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable @Min(1) Integer id) {
      studentService.deleteStudent(id);
      return ResponseEntity.ok(new ApiResponse<>(
         true,
         "student successfully removed",
         "{}"));
   }

   @GetMapping
   public ResponseEntity<Map<String, Object>> studentList() {
      var users = studentService.studentList();

      Map<String, Object> response = new HashMap<>();
      response.put("data", users);
      response.put("meta", Map.of("total", users.size()));
      response.put("success", true);

      return ResponseEntity.ok(response);
   }

   @GetMapping("/{id}")
   public ResponseEntity<ApiResponse<StudentResponseDto>> getStudent(@PathVariable @Min(1) Integer id) {
      var responseDto = studentService.getStudent(id);

      return ResponseEntity.ok(new ApiResponse<>(
         true,
         "Student data: ",
         responseDto));
   }

   @PatchMapping("/{id}")
   public ResponseEntity<StudentResponseDto> partialUpdate(@PathVariable Integer id, @Valid @RequestBody StudentPatchDto patch) {
      var response = studentService.patchStudent(id, patch);
      return ResponseEntity.ok(response);
   }
}
