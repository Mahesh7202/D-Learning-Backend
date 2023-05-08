package com.elearning.teacherservice.repository;

import com.elearning.teacherservice.model.AssignCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignCoursesRepository extends JpaRepository<AssignCourses, String> {

     void deleteByEmcode(String emcode);

    AssignCourses findByEmcode(String emcode);

    AssignCourses findByTeacherId(String teacherId);
}
