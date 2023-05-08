package com.elearning.teacherservice.repository;

import com.elearning.teacherservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    Teacher findByTeacherId(String teacherId);

    void deleteByTeacherId(String teacherId);

    Teacher findByEmcode(String emcode);
}
