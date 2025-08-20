package com.dlvi.studentmanagement.service;

import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.dto.StudentRequestDto;
import com.dlvi.studentmanagement.dto.StudentResponseDto;

import java.util.List;

public interface StudentService {

   StudentResponseDto registerStudent(StudentRequestDto student);

   void deleteStudent(Integer id);

   List<StudentResponseDto> studentList();

   StudentResponseDto getStudent(Integer id);

   StudentResponseDto patchStudent(Integer id, StudentPatchDto req);

}
