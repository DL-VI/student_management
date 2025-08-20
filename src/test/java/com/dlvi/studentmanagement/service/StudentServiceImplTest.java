package com.dlvi.studentmanagement.service;

import com.dlvi.studentmanagement.dao.StudentDao;
import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.dto.StudentRequestDto;
import com.dlvi.studentmanagement.exception.DatabaseOperationException;
import com.dlvi.studentmanagement.exception.EmailAlreadyExistsException;
import com.dlvi.studentmanagement.exception.ResourceNotFoundException;
import com.dlvi.studentmanagement.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

   @Mock
   private StudentDao studentDao;

   @InjectMocks
   private StudentServiceImpl studentService;

   @Test
   void registerStudentSuccessful() {
      StudentRequestDto req = new StudentRequestDto(
         "Maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man");

      Student student = new Student(req);
      student.setId(1);

      when(studentDao.emailExists(req.email())).thenReturn(false);
      when(studentDao.save(any(Student.class))).thenReturn(1);
      when(studentDao.getById(1)).thenReturn(Optional.of(student));

      var res = studentService.registerStudent(req);

      assertAll(
         () -> assertEquals(1, res.id()),
         () -> assertEquals("Maximo", res.name()),
         () -> assertEquals("maximo@gmail.com", res.email())
      );
   }

   @Test
   void registerStudentEmailAlreadyExists() {
      StudentRequestDto req = new StudentRequestDto(
         "Maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man");

      when(studentDao.emailExists(req.email())).thenReturn(true);

      assertThrows(EmailAlreadyExistsException.class,
         () -> studentService.registerStudent(req));

      verify(studentDao, never()).save(any());
      verify(studentDao, never()).getById(any());
   }

   @Test
   void registerStudentFailed() {
      StudentRequestDto req = new StudentRequestDto(
         "maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man");

      when(studentDao.emailExists(req.email())).thenReturn(false);
      when(studentDao.save(any(Student.class))).thenThrow(new DatabaseOperationException("The record could not be inserted into the database"));
      verify(studentDao, never()).getById(anyInt());

      assertThrows(DatabaseOperationException.class,
         () -> studentService.registerStudent(req));
   }

   @Test
   void studentNotFoundAfterSave() {
      StudentRequestDto req = new StudentRequestDto(
         "Maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man");

      when(studentDao.emailExists(req.email())).thenReturn(false);
      when(studentDao.save(any(Student.class))).thenReturn(1);
      when(studentDao.getById(1)).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class,
         () -> studentService.registerStudent(req));
   }

   @Test
   void deleteStudent() {
      when(studentDao.deleteById(anyInt())).thenReturn(1);
      assertDoesNotThrow(() -> studentService.deleteStudent(4));
   }

   @Test
   void deleteStudentFail() {
      when(studentDao.deleteById(anyInt())).thenReturn(0);
      assertThrows(ResourceNotFoundException.class,
         () -> studentService.deleteStudent(3));
   }

   @Test
   void studentList() {
      var studentList = List.of(new Student(
         1, "Maximo", "maximo@gmail.com", LocalDate.now(), "Man"));
      when(studentDao.listAll()).thenReturn(studentList);

      var response = studentService.studentList();

      assertEquals(1, response.size());

      var student = response.get(0);

      assertAll(
         () -> assertEquals(1, student.id()),
         () -> assertEquals("Maximo", student.name()),
         () -> assertEquals("maximo@gmail.com", student.email())
      );
   }

   @Test
   void studentListFail() {
      List<Student> studentList = new ArrayList<>();

      when(studentDao.listAll()).thenReturn(studentList);

      var response = studentService.studentList();
      assertTrue(response.isEmpty());
   }

   @Test
   void getStudentSuccessful() {
      var req = new StudentRequestDto(
         "Maximo",
         "maximo@gmail.com",
         LocalDate.now(),
         "Man");

      Student student = new Student(req);
      student.setId(1);

      when(studentDao.getById(anyInt())).thenReturn(Optional.of(student));

      var response = studentService.getStudent(1);

      assertAll(
         () -> assertEquals(1, response.id()),
         () -> assertEquals("Maximo", response.name()),
         () -> assertEquals("maximo@gmail.com", response.email())
      );
   }

   @Test
   void getStudentFailed() {
      when(studentDao.getById(anyInt())).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class,
         () -> studentService.getStudent(1));
   }

   @Test
   void patchStudentSuccessful() {
      StudentPatchDto patch = new StudentPatchDto("kaka", "kaka@gmail.com");
      Student student = new Student();
      student.setId(1);
      student.setName("kaka");
      student.setEmail("kaka@gmail.com");

      when(studentDao.emailExists(patch.email())).thenReturn(false);
      when(studentDao.partialUpdate(anyInt(), eq(patch))).thenReturn(1);
      when(studentDao.getById(anyInt())).thenReturn(Optional.of(student));

      var response = studentService.patchStudent(1, patch);

      assertAll(
         () -> assertEquals(1, response.id()),
         () -> assertEquals("kaka", response.name()),
         () -> assertEquals("kaka@gmail.com", response.email())
      );
   }

   @Test
   void patchStudentNotUpdated() {
      StudentPatchDto patch = new StudentPatchDto("kaka", "kaka@gmail.com");

      when(studentDao.partialUpdate(anyInt(), eq(patch))).thenReturn(0);

      assertThrows(ResourceNotFoundException.class,
         () -> studentService.patchStudent(1, patch));

      verify(studentDao, never()).getById(anyInt());
   }

   @Test
   void patchStudentUpdatedButNotFoundAfter() {
      StudentPatchDto patch = new StudentPatchDto("kaka", "kaka@gmail.com");
      Student student = new Student();
      student.setId(1);
      student.setName("kaka");
      student.setEmail("kaka@gmail.com");

      when(studentDao.partialUpdate(anyInt(), eq(patch))).thenReturn(1);
      when(studentDao.getById(anyInt())).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class,
         () -> studentService.patchStudent(1, patch));
   }

   @Test
   void patchStudentUpdateButEmailExist() {
      StudentPatchDto patch = new StudentPatchDto("kaka", "kaka@gmail.com");

      when(studentDao.emailExists(patch.email())).thenReturn(true);

      assertThrows(EmailAlreadyExistsException.class,
         () -> studentService.patchStudent(1, patch));

      verify(studentDao, never()).partialUpdate(1, patch);
   }
}
