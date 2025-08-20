package com.dlvi.studentmanagement.dao;

import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.exception.DatabaseOperationException;
import com.dlvi.studentmanagement.model.Student;

public interface StudentDao extends GenericDAO<Student, Integer> {

   boolean emailExists(String email) throws DatabaseOperationException;

   Integer partialUpdate(Integer id, StudentPatchDto patch) throws DatabaseOperationException;

}
