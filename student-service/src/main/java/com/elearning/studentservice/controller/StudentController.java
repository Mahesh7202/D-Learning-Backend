package com.elearning.studentservice.controller;


import com.elearning.studentservice.dto.StudentRequest;
import com.elearning.studentservice.dto.StudentResponse;
import com.elearning.studentservice.exception.UserNotCreated;
import com.elearning.studentservice.exception.UserNotFound;
import com.elearning.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static com.elearning.studentservice.model.Enum.ErrorConstants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
//@CrossOrigin("*")
@Slf4j
public class StudentController {

    private final StudentService studentService;


    @PostMapping
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest studentRequest){
        log.info("Inside [/student/createStudent], request to create Student");
        StudentResponse studentResponse = studentService.saveStudent(studentRequest);
        if(studentResponse==null){
            log.error("Task of creation of Student was Failed");
            return ResponseEntity.badRequest().body(new UserNotCreated(ERROR_CREATING_STUDENT));
        }
        log.info("Task of creation of Student was Completed");
        return ResponseEntity.ok().body(studentResponse);
    }

    @GetMapping
    @RolesAllowed({"ADMIN","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllStudents(){
        log.info("Inside [/student/getAllStudents], request to get All Students");
        List<StudentResponse> studentResponses = studentService.findAllStudents();
        if(studentResponses == null){
            log.error("Task to get All Students Failed");
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_STUDENT_FOUND));
        }
        log.error("Task of get All Students Completed");
        return ResponseEntity.ok().body(studentResponses);
    }

    @GetMapping("{studentId}")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getStudentByStudentId(@PathVariable("studentId") String studentId){
        log.info("Inside [/student/getStudentByStudentId], request to get Student by StudentId: {}",studentId);
        StudentResponse studentResponse = studentService.findStudentByStudentId(studentId);
        if(studentResponse==null){
            log.error("Task to get Student by Student Id: {} is Failed",studentId);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_STUDENT_FOUND));
        }
        log.info("Task to get Student by Student Id: {} is Completed",studentId);
        return ResponseEntity.ok().body(studentResponse);
    }

    @GetMapping("/htno/{htno}")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getStudentByHtno(@PathVariable("htno") String htno){
        log.info("Inside [/student/getStudentByStudentId], request to get Student by HallTicket No: {}",htno);
        StudentResponse studentResponse = studentService.findStudentByHallTicketNo(htno);
        if(studentResponse==null){
            log.error("Task to get Student by HallTicket No: {} is Failed",htno);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_STUDENT_FOUND));
        }
        log.info("Task to get Student by HallTicket No: {} is Completed",htno);
        return ResponseEntity.ok().body(studentResponse);
    }

    @GetMapping("/type")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getStudentByType(@RequestParam("department") Integer department, @RequestParam("branch") Integer branch){
        log.info("Inside [/student/type/getStudentByType], request to get Student by Department: {} and Branch: {}",department, branch);
        List<StudentResponse> studentResponse = studentService.findStudentByType(department, branch);
        if(studentResponse==null){
            log.error("Task to get Student by Department: {} and Branch: {} is Failed",department, branch);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_NO_STUDENT_FOUND));
        }
        log.info("Task to get Student by Department: {} and Branch: {} is Completed",department, branch);
        return ResponseEntity.ok().body(studentResponse);
    }

    @PutMapping("{studentId}")
    @RolesAllowed({"ADMIN","STUDENT"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateStudentByStudentId(@PathVariable("studentId") String studentId, @RequestBody StudentRequest studentRequest){
        log.info("Inside [/student/updateStudentByStudentId], request to update Student by StudentId: {}",studentId);
        StudentResponse studentResponse = studentService.updateStudent(studentId, studentRequest);
        if(studentResponse==null){
            log.error("Task to Update Student by Student Id: {} is Failed",studentId);
            return ResponseEntity.badRequest().body(new UserNotFound(ERROR_UPDATING_STUDENT));
        }
        log.info("Task to get Student by Student Id: {} is Completed",studentId);
        return ResponseEntity.ok().body(studentResponse);
    }

    @DeleteMapping("{studentId}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudentByStudentId(@PathVariable("studentId") String studentId){
        log.info("Inside [/student/deleteStudentByStudentId], request to delete Student by StudentId: {}",studentId);
        studentService.deleteStudentByStudentId(studentId);
        log.info("Task to Delete Student for Student Id: {} was Completed",studentId);
    }

}
