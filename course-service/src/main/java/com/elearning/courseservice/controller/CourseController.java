package com.elearning.courseservice.controller;


import com.elearning.courseservice.dto.CourseRequest;
import com.elearning.courseservice.dto.CourseResponse;
import com.elearning.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/course")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class CourseController {


    private final CourseService courseService;


    @PostMapping
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest courseRequest){
        log.info("Inside [/course/createCourse], request to create User");
        CourseResponse courseResponse = courseService.saveCourse(courseRequest);
        if(courseResponse==null){
            log.error("Task of creation of User was Failed");
            return null;
        }
        log.info("Task of creation of User was Completed");
        return ResponseEntity.ok().body(courseResponse);
    }

    @GetMapping
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllCourses(){
        log.info("Inside [/course/getAllCourses], request to Retrieve All Courses");
        List<CourseResponse> courseResponse = courseService.findAllCourses();
        if(courseResponse==null){
            log.error("Task of Retrieve All Courses was Failed");
            return null;
        }
        log.info("Task of Retrieve All Courses was Completed");
        return ResponseEntity.ok().body(courseResponse);
    }


    @GetMapping("/coursetype/{coursetype}")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCoursesByCourseType(@PathVariable("coursetype") Integer coursetype){
        log.info("Inside [/course/getCoursesByCourseType], request to get Course by CourseType");
        List<CourseResponse> courseResponse = courseService.findCoursesByCourseType(coursetype);
        if(courseResponse==null){
            log.error("Task to get Courses by CourseType: {} is Failed",coursetype);
            return null;
        }
        log.info("Task to get Courses by CourseType: {} is Completed",coursetype);
        return ResponseEntity.ok().body(courseResponse);
    }

    @GetMapping("/{sucode}")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourseByCourseId(@PathVariable("sucode") String sucode){
        log.info("Inside [/course/getCourseByCourseId], request to get Course by SUCODE");
        CourseResponse courseResponse = courseService.findCourseById(sucode);
        if(courseResponse==null){
            log.error("Task to get Course by SUCODE: {} is Failed",sucode);
            return null;
        }
        log.info("Task to get Course by SUCODE: {} is Completed",sucode);
        return ResponseEntity.ok().body(courseResponse);
    }


    @PutMapping("{sucode}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCourse(@PathVariable("sucode") String sucode, @RequestBody CourseRequest courseRequest){
        log.info("Inside [/course/updateCourse], request to Upadate Course by SUCODE");
        CourseResponse courseResponse = courseService.updateCourse(sucode, courseRequest);
        if(courseResponse==null){
            log.error("Task to Update Course by SUCODE: {} is Failed",sucode);
            return null;
        }
        log.info("Task to get Course by SUCODE: {} is Completed",sucode);
        return ResponseEntity.ok().body(courseResponse);
    }

    @DeleteMapping("{sucode}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourseByCourseId(@PathVariable("sucode") String sucode){
        log.info("Inside [/course/addUser], request to Delete Course by SUCODE");
        courseService.deleteCourseByCourseId(sucode);
        log.info("Task to Delete Course for SUCODE: {} was Completed",sucode);
    }



}
