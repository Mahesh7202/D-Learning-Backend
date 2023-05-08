package com.elearning.studentservice.repository;

import com.elearning.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Student findByStudentId(String studentId);
    Student findByHtno(String htno);
    
    
    void deleteByStudentId(String studentId);

    List<Student> findByDepartmentAndBranch(Integer department, Integer branch);
}
