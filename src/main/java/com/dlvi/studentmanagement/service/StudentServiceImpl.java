package com.dlvi.studentmanagement.service;

import com.dlvi.studentmanagement.dao.StudentDao;
import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.dto.StudentRequestDto;
import com.dlvi.studentmanagement.dto.StudentResponseDto;
import com.dlvi.studentmanagement.exception.EmailAlreadyExistsException;
import com.dlvi.studentmanagement.exception.ResourceNotFoundException;
import com.dlvi.studentmanagement.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

   private static final String ID_NOT_FOUND = "Student not found with id ";
   private final StudentDao studentDao;

   public StudentServiceImpl(StudentDao studentDao) {
      this.studentDao = studentDao;
   }

   @Override
   public StudentResponseDto registerStudent(StudentRequestDto req) {
      if (studentDao.emailExists(req.email()))
         throw new EmailAlreadyExistsException("The email " + req.email() + " is already registered");

      Integer id = studentDao.save(new Student(req));

      return studentDao.getById(id)
         .map(student -> new StudentResponseDto(student.getId(), student.getName(), student.getEmail()))
         .orElseThrow(() -> new ResourceNotFoundException(ID_NOT_FOUND + id));
   }

   @Override
   public void deleteStudent(Integer id) {
      if (studentDao.deleteById(id) == 0)
         throw new ResourceNotFoundException(ID_NOT_FOUND + id);
   }

   @Override
   public List<StudentResponseDto> studentList() {
      return studentDao.listAll().stream()
         .map(student -> new StudentResponseDto(
            student.getId(),
            student.getName(),
            student.getEmail()
         ))
         .toList();
   }

   @Override
   public StudentResponseDto getStudent(Integer id) {
      return studentDao.getById(id)
         .map(student -> new StudentResponseDto(
            student.getId(),
            student.getName(),
            student.getEmail()
         ))
         .orElseThrow(() -> new ResourceNotFoundException(ID_NOT_FOUND + id));

   }

   @Override
   public StudentResponseDto patchStudent(Integer id, StudentPatchDto patch) {
      if (studentDao.emailExists(patch.email()))
         throw new EmailAlreadyExistsException("The email " + patch.email() + " is already registered");

      if (studentDao.partialUpdate(id, patch) == 0)
         throw new ResourceNotFoundException("The record could not be updated into the database");

      return studentDao.getById(id).map(student -> new StudentResponseDto(
            student.getId(),
            student.getName(),
            student.getEmail()))
         .orElseThrow(() -> new ResourceNotFoundException(ID_NOT_FOUND + id));
   }
}
