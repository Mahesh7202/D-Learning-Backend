package com.elearning.studentservice.service;


import com.elearning.studentservice.dto.StudentRequest;
import com.elearning.studentservice.dto.StudentResponse;
import com.elearning.studentservice.dto.User;
import com.elearning.studentservice.exception.RestException;
import com.elearning.studentservice.exception.UserNotCreated;
import com.elearning.studentservice.model.Student;
import com.elearning.studentservice.repository.StudentRepository;
import com.elearning.studentservice.service.client.RestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.elearning.studentservice.model.Enum.ErrorConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final RestTemplateClient restTemplateClient;

    private final Tracer tracer;

    public StudentResponse saveStudent(StudentRequest studentRequest){
        if(studentRequest == null){
            log.error("Student Request is empty: Request Body");
            throw new RestException(ERROR_NO_BODY);
        }
        log.info("Request to create Student by studentId: {}",studentRequest.getStudentId());
        Student student = Student.builder()
                .studentId(studentRequest.getStudentId())
                        .fname(studentRequest.getFname())
                        .lname(studentRequest.getLname())
                        .email(studentRequest.getEmail())
                        .address(studentRequest.getAddress())
                        .branch(studentRequest.getBranch())
                        .department(studentRequest.getDepartment())
                        .password(studentRequest.getPassword())
                        .semno(studentRequest.getSemno())
                        .city(studentRequest.getCity())
                        .phonenumber(studentRequest.getPhonenumber())
                        .age(studentRequest.getAge())
                        .build();

        Student save = studentRepository.save(student);
        if(save != null){

            Span keycloakSpan = tracer.nextSpan().name("keycloakUserSpan");

            try(Tracer.SpanInScope inScope = tracer.withSpan(keycloakSpan.start())){
                log.info("Creation of Student of Student Id: {}",save.getStudentId());
                log.info("[keycloak-server] Sending request to Keycloak Server to create a User");
                User user = User.builder()
                        .emailId(save.getEmail())
                        .firstname(save.getFname())
                        .lastName(save.getLname())
                        .password(save.getPassword())
                        .userName(save.getHtno())
                        .role("STUDENT")
                        .build();
                log.info("User Username:{} Normal Username == Hall Ticketno :{}",user.getUserName(), save.getHtno());
                String keyCloakUser = restTemplateClient.createKeyCloakUser(user);

                if(keyCloakUser==null){
                    log.error("[keycloak-server] Cannot create User");
                    throw new UserNotCreated(ERROR_CREATING_USER);
                }
                log.info("[keycloak-server] Creation of User was Successful");
                return mapToStudentResponse(save);
            }finally{
                keycloakSpan.end();
            }
        }
        log.error("Unable to create Student for StudentId: {}",studentRequest.getStudentId());
        return null;
    }

    public StudentResponse findStudentByStudentId(String studentId){
        log.info("Retrieve Student by StudentId: {}", studentId);
        Student student = studentRepository.findByStudentId(studentId);
        if(student==null){
            log.error("Unable to find the Student with Student ID: {}",studentId);
            return null;
        }
        log.info("Student was retrieve successful");
        return mapToStudentResponse(student);
    }

    public StudentResponse findStudentByHallTicketNo(String htno) {
        log.info("Retrieve Student by HallTicket No: {}", htno);
        Student student = studentRepository.findByHtno(htno);
        if(student==null){
            log.error("Unable to find the Student with HallTicket No: {}",htno);
            return null;
        }
        log.info("Student was retrieve successful");
        return mapToStudentResponse(student);

    }

    public List<StudentResponse> findStudentByType(Integer department, Integer branch) {
        log.info("Retrieve Student by Department: {} and Branch: {}", department, branch);
        List<Student> students = studentRepository.findByDepartmentAndBranch(department, branch);
        if(students==null){
            log.error("Unable to find the Student by Department: {} and Branch: {}", department, branch);
            return null;
        }
        log.info("Student was retrieve successful");
        return students.stream().map(this::mapToStudentResponse).collect(Collectors.toList());

    }

    public List<StudentResponse> findAllStudents(){
        log.info("Retrieve All Students");
        List<Student> students = studentRepository.findAll();
        if(students.isEmpty()){
            log.error("Unable to find the Students");
            return null;
        }
        log.info("Retrieval of All Students was Successful");
        return students.stream().map(this::mapToStudentResponse).collect(Collectors.toList());
    }



    public StudentResponse updateStudent(String studentId,StudentRequest studentRequest){
        if(studentRequest == null){
            log.error("Student Request is empty: Request Body");
            throw new RestException(ERROR_NO_BODY);
        }
        log.info("Updating Student of StudentId: {}",studentId);
        Student student = studentRepository.findByStudentId(studentId);

                student.setFname(studentRequest.getFname());
                student.setLname(studentRequest.getLname());
                student.setEmail(studentRequest.getEmail());
                student.setBranch(studentRequest.getBranch());
                student.setHtno(studentRequest.getHtno());
                student.setAddress(studentRequest.getAddress());
                student.setDepartment(studentRequest.getDepartment());
                student.setPassword(studentRequest.getPassword());
                student.setAge(studentRequest.getAge());
                student.setSemno(studentRequest.getSemno());

        log.info("[keycloak-server] Sending request to Keycloak Server to Update a User");

        User user = User.builder()
                .emailId(studentRequest.getEmail())
                .firstname(studentRequest.getFname())
                .lastName(studentRequest.getLname())
                .password(studentRequest.getPassword())
                .userName(studentRequest.getHtno())
                .role("STUDENT")
                .build();

            String temp = restTemplateClient.updateKeyCloakUser(getKeyCloakUserId(student), user);
            if(temp==null){
                log.error("[keycloak-server] Updating of User was Failed");
                throw new RestException(ERROR_UPDATING_USER);
            }
        log.info("[keycloak-server] Updating of User was Successful");

        Student student1 = studentRepository.save(student);
        if(student1==null){
            log.error("Cannot able to update Student for StudentId: {}",student.getStudentId());
            return null;
        }
        log.info("Updated Student of Student Id: {}",student1.getStudentId());
        return mapToStudentResponse(student1);
    }

    @Transactional
    public void deleteStudentByStudentId(String studentId){
        log.info("Delete Student of StudentId: {}",studentId);
        String userId = getKeyCloakUserId(studentRepository.findByStudentId(studentId));
        log.info("[keycloak-server] Sending request to keycloak-server to delete a user of userId: {}",userId);
        studentRepository.deleteByStudentId(studentId);
        log.info("Deleted Student of StudendtId: {}", studentId);
        Boolean status = restTemplateClient.deleteKeyCloakUser(userId);
        if(!status){
            log.info("Deletion of User for userId: {} was failed",userId);
            throw new RestException(ERROR_DELETING_USER);
        }
    }



    public String getKeyCloakUserId(Student student){
        log.info("Using Student get the User Id from Keycloak-server");
        List<UserRepresentation> userRepresentations = restTemplateClient.getKeyCloakUser(student.getHtno().toLowerCase());
        UserRepresentation userRepresentation = userRepresentations.stream()
                .filter(users ->  users.getEmail().equals(student.getEmail().toLowerCase()))
                .findFirst()
                .get();
        String userId = userRepresentation.getId();
        if(userId==null){
            log.info("User id not found");
            throw new RestException(ERROR_NO_USER_FOUND);
        }
        log.info("User id :{}",userId);
        return userId;
    }

    private StudentResponse mapToStudentResponse(Student student){
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .fname(student.getFname())
                .lname(student.getLname())
                .email(student.getEmail())
                .htno(student.getHtno())
                .address(student.getAddress())
                .branch(student.getBranch())
                .department(student.getDepartment())
                .password(student.getPassword())
                .semno(student.getSemno())
                .city(student.getCity())
                .phonenumber(student.getPhonenumber())
                .age(student.getAge())
                .build();
    }



}
