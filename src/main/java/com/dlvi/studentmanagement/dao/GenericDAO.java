package com.dlvi.studentmanagement.dao;

import com.dlvi.studentmanagement.exception.DatabaseOperationException;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, I> {

   Integer save(T t) throws DatabaseOperationException;

   Integer deleteById(I id) throws DatabaseOperationException;

   List<T> listAll() throws DatabaseOperationException;

   Optional<T> getById(I id) throws DatabaseOperationException;

}
