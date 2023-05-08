package com.elearning.courseservice.repository;

import com.elearning.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Course findBySucode(String sucode);
    List<Course> findByCoursetype(Integer coursetype);
}
