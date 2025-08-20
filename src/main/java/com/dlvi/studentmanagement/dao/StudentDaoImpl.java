package com.dlvi.studentmanagement.dao;

import com.dlvi.studentmanagement.dto.StudentPatchDto;
import com.dlvi.studentmanagement.exception.DatabaseOperationException;
import com.dlvi.studentmanagement.model.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDaoImpl implements StudentDao {
   private final JdbcTemplate jdbcTemplate;
   private final RowMapper<Student> rowMapper = (rs, rowNum) -> {
      Student student = new Student();
      student.setId(rs.getInt("id"));
      student.setName(rs.getString("name"));
      student.setEmail(rs.getString("email"));
      student.setBirthDate(rs.getDate("birth_date").toLocalDate());
      student.setSex(rs.getString("sex"));
      return student;
   };

   public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   @Override
   public Integer save(Student student) throws DatabaseOperationException {
      try {
         String sql = "insert into student (name, email, birth_date, sex) values (?,?,?,?)";
         KeyHolder key = new GeneratedKeyHolder();
         int insert = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setObject(3, student.getBirthDate());
            ps.setString(4, student.getSex());
            return ps;
         }, key);

         Number generatedKey = key.getKey();

         if (insert == 0 || generatedKey == null)
            throw new DatabaseOperationException("The record was not inserted, no key generated.");
         else
            return generatedKey.intValue();
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("The record could not be inserted into the database", e);
      }
   }

   @Override
   public Integer deleteById(Integer id) throws DatabaseOperationException {
      try {
         return jdbcTemplate.update(
            "delete from student where id = ?",
            id);
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("The record with ID " + id + " could not be deleted from the database", e);
      }
   }

   @Override
   public List<Student> listAll() throws DatabaseOperationException {
      try {
         return jdbcTemplate.query(
            "select * from student",
            rowMapper);
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("The records could not be retrieved from the database", e);
      }
   }

   @Override
   public Optional<Student> getById(Integer id) throws DatabaseOperationException {
      try {
         String sql = "select * from student where id = ?";
         List<Student> students = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);

         return students.stream()
            .map(student -> new Student(
               student.getId(),
               student.getName(),
               student.getEmail(),
               student.getBirthDate(),
               student.getSex()))
            .findFirst();
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("The record with ID " + id + " could not be retrieved from the database", e);
      }
   }

   @Override
   public boolean emailExists(String email) throws DatabaseOperationException {
      try {
         String sql = "select exists (select 1 from student where email = ?)";
         Boolean exist = jdbcTemplate.queryForObject(sql, Boolean.class, email);
         return Boolean.TRUE.equals(exist);
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("An error occurred while checking if the email '" + email + "' exists in the database", e);
      }
   }

   @Override
   public Integer partialUpdate(Integer id, StudentPatchDto req) throws DatabaseOperationException {
      try {
         return jdbcTemplate.update(
            "update student set name = ?, email = ? where id = ?",
            req.name(),
            req.email(),
            id);
      } catch (DataAccessException e) {
         throw new DatabaseOperationException("The record could not be partially updated in the database", e);
      }
   }
}
