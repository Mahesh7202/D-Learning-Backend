package com.elearning.teacherservice.controller;


import com.elearning.teacherservice.dto.AssignCoursesReq;
import com.elearning.teacherservice.dto.TeacherRequest;
import com.elearning.teacherservice.dto.TeacherResponse;
import com.elearning.teacherservice.exception.UserNotCreated;
import com.elearning.teacherservice.exception.UserNotFound;
import com.elearning.teacherservice.repository.AssignCoursesRepository;
import com.elearning.teacherservice.repository.TeacherRepository;
import com.elearning.teacherservice.service.AssignCoursesService;
import com.elearning.teacherservice.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static com.elearning.teacherservice.model.Enum.ErrorConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
//@CrossOrigin("*")
public class TeacherController {
    private final AssignCoursesRepository assignCoursesRepository;

    private final TeacherService teacherService;

    private final AssignCoursesService assignCoursesService;

    @PostMapping
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTeacher(@RequestBody TeacherRequest teacherRequest) {
        log.info("Inside [/teacher/createTeacher], request to create Teacher");
        TeacherResponse teacherResponse = teacherService.saveTeacher(teacherRequest);
        if (teacherResponse == null) {
            log.error("Task of creation of Teacher was Failed");
            return ResponseEntity.badRequest().body(new UserNotCreated(ERROR_CREATING_TEACHER));
        }
        log.info("Task of creation of Teacher was Completed");
        return ResponseEntity.ok().body(teacherResponse);
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTeachers() {
        log.info("Inside [/teacher/getAllTeachers], request to get All Teachers");
        List<TeacherResponse> teacherResponses = teacherService.findAllTeachers();
        if (teacherResponses == null) {
            log.error("Task to get All Teachers Failed");
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_TEACHER_FOUND));
        }
        log.error("Task of get All Teachers Completed");
        return ResponseEntity.ok().body(teacherResponses);
    }

    @GetMapping("{teacherId}")
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTeacherByTeacherId(@PathVariable("teacherId") String teacherId) {
        log.info("Inside [/teacher/getTeacherByTeacherId], request to get Teacher by teacherId: {}", teacherId);
        TeacherResponse teacherResponse = teacherService.findTeacherByTeacherId(teacherId);
        if (teacherResponse == null) {
            log.error("Task to get Teacher by Teacher Id: {} is Failed", teacherId);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_TEACHER_FOUND));
        }
        log.info("Task to get Teacher by Teacher Id: {} is Completed", teacherId);
        return ResponseEntity.ok().body(teacherResponse);
    }

    @GetMapping("/emcode/{emcode}")
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTeacherByEmcode(@PathVariable("emcode") String emcode){
        log.info("Inside [/Teacher/getTeacherByEmcode], request to get Teacher by EmployeeCode No: {}",emcode);
        TeacherResponse teacherResponse = teacherService.findTeacherByEmcode(emcode);
        if(teacherResponse==null){
            log.error("Task to get Teacher by EmployeeCode No: {} is Failed",emcode);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_TEACHER_FOUND));
        }
        log.info("Task to get Teacher by EmployeeCode No: {} is Completed",emcode);
        return ResponseEntity.ok().body(teacherResponse);
    }


    @PutMapping("{teacherId}")
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateTeacherByTeacherId(@PathVariable("teacherId") String teacherId, @RequestBody TeacherRequest teacherRequest) {
        log.info("Inside [/teacher/updateTeacherByTeacherId], request to update Teacher by teacherId: {}", teacherId);
        TeacherResponse teacherResponse = teacherService.updateTeacher(teacherId, teacherRequest);
        if (teacherResponse == null) {
            log.error("Task to Update Teacher by Teacher Id: {} is Failed", teacherId);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_UPDATING_TEACHER));
        }
        log.info("Task to get Teacher by Teacher Id: {} is Completed", teacherId);
        return ResponseEntity.ok().body(teacherResponse);
    }

    @DeleteMapping("{teacherId}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacherByTeacherId(@PathVariable("teacherId") String teacherId) {
        log.info("Inside [/teacher/deleteTeacherByTeacherId], request to delete Teacher by teacherId: {}", teacherId);
        teacherService.deleteTeacherByTeacherId(teacherId);
        log.info("Task to Delete Teacher for Teacher Id: {} was Completed", teacherId);
    }



    @GetMapping("/courses/{emcode}")
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTeacherCoursesByEmcode(@PathVariable("emcode") String emcode){
        log.info("Inside [/teacher/courses/emcode/getTeacherCoursesByEmcode], request to get Teacher by EmployeeCode No: {}",emcode);
        AssignCoursesReq assignCoursesReq = assignCoursesService.findAssignCoursesByEmcode(emcode);
        if(assignCoursesReq==null){
            log.error("Task to get Teacher by EmployeeCode No: {} is Failed",emcode);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_TEACHER_FOUND));
        }
        log.info("Task to get Teacher by EmployeeCode No: {} is Completed",emcode);
        return ResponseEntity.ok().body(assignCoursesReq);
    }

    @GetMapping("/courses")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTeachersCourses() {
        log.info("Inside [/teacher/courses/getAllTeachersCourses], request to get All Teachers Courses");
        List<AssignCoursesReq> assignCourses = assignCoursesService.findAllAssignCourses();
        if (assignCourses == null) {
            log.error("Task to get All Teachers Failed");
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_TEACHER_FOUND));
        }
        log.error("Task of get All Teachers Completed");
        return ResponseEntity.ok().body(assignCourses);
    }

    @PutMapping("/courses/{teacherId}")
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateTeacherCoursesByTeacherId(@PathVariable("teacherId") String teacherId, @RequestBody AssignCoursesReq assignCoursesReq) {
        log.info("Inside [/teacher/courses/updateTeacherCoursesByEmcode], request to update Teacher by teacherId: {}", teacherId);
        AssignCoursesReq assignCourses = assignCoursesService.updateAssignCourses(teacherId, assignCoursesReq);
        if (assignCourses == null) {
            log.error("Task to Update Teacher by Teacher Id: {} is Failed", teacherId);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_UPDATING_TEACHER));
        }
        log.info("Task to get Teacher by Teacher Id: {} is Completed", teacherId);
        return ResponseEntity.ok().body(assignCourses);
    }

    @DeleteMapping("/courses/{emcode}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacherCoursesByEmcode(@PathVariable("emcode") String emcode) {
        log.info("Inside [/teacher/courses/deleteTeacherCoursesByEmcode], request to delete Teacher by teacherId: {}", emcode);
        assignCoursesService.deleteAssignCoursesByEmcode(emcode);
        log.info("Task to Delete Teacher for Teacher Id: {} was Completed", emcode);
    }


}