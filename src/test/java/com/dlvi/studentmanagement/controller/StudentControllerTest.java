package com.dlvi.studentmanagement.controller;

import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.dto.StudentRequestDto;
import com.dlvi.studentmanagement.dto.StudentResponseDto;
import com.dlvi.studentmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class)
class StudentControllerTest {

   @Autowired
   private MockMvc mockMvc;

   /*
    * in this way, we avoid using @MockBean which is deprecated and marked "forRemoval = true"
    * @MockBean
    * private StudentService studentService;
    */
   @Autowired
   private StudentService studentService;

   @TestConfiguration
   static class MockServiceConfig {
      @Bean
      public StudentService studentService() {
         return mock(StudentService.class);
      }
   }

   // it is used when the endpoint expects a @RequestBody
   @Autowired
   private ObjectMapper objectMapper;


   @Test
   void registerStudent() throws Exception {
      var req = new StudentRequestDto(
         "Maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man"
      );

      var res = new StudentResponseDto(
         1,
         "Maximo",
         "maximo@gmail.com"
      );

      when(studentService.registerStudent(req)).thenReturn(res);

      mockMvc.perform(post("/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.data.id").value(1))
         .andExpect(jsonPath("$.data.name").value("Maximo"))
         .andExpect(jsonPath("$.data.email").value("maximo@gmail.com"));

      verify(studentService).registerStudent(req);
   }

   @Test
   void deleteStudent() throws Exception {
      doNothing().when(studentService).deleteStudent(anyInt());

      mockMvc.perform(delete("/student/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.message").value("student successfully removed"))
         .andExpect(jsonPath("$.data").value("{}"));

      verify(studentService).deleteStudent(1);
   }

   @Test
   void getAllStudentsReturnsList() throws Exception {
      List<StudentResponseDto> student = List.of(
         new StudentResponseDto(1, "Maximo", "maximo@gmail.com")
      );

      when(studentService.studentList()).thenReturn(student);

      mockMvc.perform(get("/student")
            .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.data[0].id").value(1))
         .andExpect(jsonPath("$.data[0].name").value("Maximo"))
         .andExpect(jsonPath("$.data[0].email").value("maximo@gmail.com"));

      verify(studentService).studentList();
   }

   @Test
   void getStudent() throws Exception {
      StudentResponseDto response = new StudentResponseDto(1, "Maximo", "maximo@gmail.com");

      when(studentService.getStudent(anyInt())).thenReturn(response);

      mockMvc.perform(get("/student/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.data.id").value(1))
         .andExpect(jsonPath("$.data.name").value("Maximo"))
         .andExpect(jsonPath("$.data.email").value("maximo@gmail.com"));

      verify(studentService).getStudent(1);
   }

   @Test
   void partialUpdate() throws Exception{
      var req = new StudentPatchDto(
         "Maximo",
         "maximo@gmail.com"
      );

      var res = new StudentResponseDto(
         1,
         "Maximo",
         "maximo@gmail.com"
      );

      when(studentService.patchStudent(anyInt(), eq(req))).thenReturn(res);

      mockMvc.perform(patch("/student/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
         .andExpect(jsonPath("$.id").value(1))
         .andExpect(jsonPath("$.name").value("Maximo"))
         .andExpect(jsonPath("$.email").value("maximo@gmail.com"));

      verify(studentService).patchStudent(1, req);
   }
}
