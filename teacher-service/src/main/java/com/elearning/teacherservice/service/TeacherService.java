package com.elearning.teacherservice.service;


import com.elearning.teacherservice.dto.AssignCoursesReq;
import com.elearning.teacherservice.dto.TeacherRequest;
import com.elearning.teacherservice.dto.TeacherResponse;
import com.elearning.teacherservice.dto.User;
import com.elearning.teacherservice.exception.RestException;
import com.elearning.teacherservice.exception.UserNotCreated;
import com.elearning.teacherservice.model.Teacher;
import com.elearning.teacherservice.repository.TeacherRepository;
import com.elearning.teacherservice.service.client.RestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.elearning.teacherservice.model.Enum.ErrorConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {
    
    private final TeacherRepository teacherRepository;

    private final AssignCoursesService assignCoursesService;
    private final RestTemplateClient restTemplateClient;
    private final Tracer tracer;

    public TeacherResponse saveTeacher(TeacherRequest teacherRequest){
        if(teacherRequest == null){
            log.error("Student Request is empty: Request Body");
            throw new RestException(ERROR_NO_BODY);
        }
        log.info("Request to create Student by teacherId: {}",teacherRequest.getTeacherId());
        Teacher teacher = Teacher.builder()
                .teacherId(teacherRequest.getTeacherId())
                .fname(teacherRequest.getFname())
                .lname(teacherRequest.getLname())
                .email(teacherRequest.getEmail())
                .address(teacherRequest.getAddress())
                .age(teacherRequest.getAge())
                .department(teacherRequest.getDepartment())
                .phonenumber(teacherRequest.getPhonenumber())
                .password(teacherRequest.getPassword())
                .branch(teacherRequest.getBranch())
                .build();

        Teacher save = teacherRepository.save(teacher);
        if(save != null){

            AssignCoursesReq assignCoursesReq = AssignCoursesReq.builder()
                                                .teacherId(save.getTeacherId())
                                                .emcode(save.getEmcode())
                                                .build();


            AssignCoursesReq assignCoursesReq1 = assignCoursesService.saveAssignCourses(assignCoursesReq);
            if(assignCoursesReq1 != null){
                log.info("Creation of Space for a AssignCourses of Teacher Emcode: {}", assignCoursesReq1.getEmcode());
            }

            Span keycloakSpan = tracer.nextSpan().name("keycloakUserSpan");

            try(Tracer.SpanInScope inScope = tracer.withSpan(keycloakSpan.start())){
                log.info("Creation of Student of Student Id: {}",save.getTeacherId());
                log.info("[keycloak-server] Sending request to Keycloak Server to create a User");
                User user = User.builder()
                        .emailId(save.getEmail())
                        .firstname(save.getFname())
                        .lastName(save.getLname())
                        .password(save.getPassword())
                        .userName(save.getEmcode())
                        .role("TEACHER")
                        .build();
                log.info("User Username:{} Normal Username == Hall Ticketno :{}",user.getUserName(), save.getEmcode());
                String keyCloakUser = restTemplateClient.createKeyCloakUser(user);

                if(keyCloakUser==null){
                    log.error("[keycloak-server] Cannot create User");
                    throw new UserNotCreated(ERROR_CREATING_USER);
                }
                log.info("[keycloak-server] Creation of User was Successful");
                return mapToTeacherResponse(save);
            }finally{
                keycloakSpan.end();
            }
        }
        log.error("Unable to create Student for teacherId: {}",teacher.getTeacherId());
        return null;
    }

    public TeacherResponse findTeacherByTeacherId(String teacherId){
        log.info("Retrieve Student by teacherId: {}", teacherId);
        Teacher teacher = teacherRepository.findByTeacherId(teacherId);
        if(teacher==null){
            log.error("Unable to find the Student with Student ID: {}",teacherId);
            return null;
        }
        log.info("Student was retrieve successful");
        return mapToTeacherResponse(teacher);
    }

    public TeacherResponse findTeacherByEmcode(String emcode) {
        log.info("Retrieve Teacher by HallTicket No: {}", emcode);
        Teacher teacher = teacherRepository.findByEmcode(emcode);
        if(teacher==null){
            log.error("Unable to find the Teacher with HallTicket No: {}",emcode);
            return null;
        }
        log.info("Teacher was retrieve successful");
        return mapToTeacherResponse(teacher);
    }

    public List<TeacherResponse> findAllTeachers(){

        log.info("Retrieve All Teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        if(teachers.isEmpty()){
            log.error("Unable to find the Teachers");
            return null;
        }
        log.info("Retrieval of All Teachers was Successful");
        return teachers.stream().map(this::mapToTeacherResponse).collect(Collectors.toList());
    }


    public TeacherResponse updateTeacher(String teacherId, TeacherRequest teacherRequest){
            if(teacherRequest == null){
                log.error("Student Request is empty: Request Body");
                throw new RestException(ERROR_NO_BODY);
            }
            log.info("Updating Student of teacherId: {}",teacherId);
            Teacher teacher = teacherRepository.findByTeacherId(teacherId);

        teacher.setFname(teacherRequest.getFname());
        teacher.setLname(teacherRequest.getLname());
        teacher.setEmail(teacherRequest.getEmail());
        teacher.setEmcode(teacherRequest.getEmcode());
        teacher.setAddress(teacherRequest.getAddress());
        teacher.setAge(teacherRequest.getAge());
        teacher.setDepartment(teacherRequest.getDepartment());
        teacher.setPhonenumber(teacherRequest.getPhonenumber());
        teacher.setPassword(teacherRequest.getPassword());
        teacher.setBranch(teacherRequest.getBranch());

            log.info("[keycloak-server] Sending request to Keycloak Server to Update a User");

            User user = User.builder()
                    .emailId(teacherRequest.getEmail())
                    .firstname(teacherRequest.getFname())
                    .lastName(teacherRequest.getLname())
                    .password(teacherRequest.getPassword())
                    .userName(teacherRequest.getEmcode())
                    .role("TEACHER")
                    .build();

            String temp = restTemplateClient.updateKeyCloakUser(getKeyCloakUserId(teacher), user);
            if(temp==null){
                log.error("[keycloak-server] Updating of User was Failed");
                throw new RestException(ERROR_UPDATING_USER);
            }
            log.info("[keycloak-server] Updating of User was Successful");

            Teacher teacher1 = teacherRepository.save(teacher);
            if(teacher1==null){
                log.error("Cannot able to update Student for teacherId: {}",teacher1.getTeacherId());
                return null;
            }
            log.info("Updated Student of Student Id: {}",teacher1.getTeacherId());
            return mapToTeacherResponse(teacher1);
        }

        @Transactional
    public void deleteTeacherByTeacherId(String teacherId){
        log.info("Delete Student of teacherId: {}",teacherId);
        Teacher teacher = teacherRepository.findByTeacherId(teacherId);
        String userId = getKeyCloakUserId(teacher);
        log.info("[keycloak-server] Sending request to keycloak-server to delete a user of userId: {}",userId);
        teacherRepository.deleteByTeacherId(teacherId);
        log.info("Deleted Student of StudendtId: {}", teacherId);
        Boolean status = restTemplateClient.deleteKeyCloakUser(userId);

        log.info("Deleting the Space of Teacher Assigned Courses for Emcode: {}",teacher.getEmcode());
        assignCoursesService.deleteAssignCoursesByEmcode(teacher.getEmcode());
        if(!status){
            log.info("Deletion of User for userId: {} was failed",userId);
            throw new RestException(ERROR_DELETING_USER);
        }
    }


    public String getKeyCloakUserId(Teacher teacher){
        log.info("Using Student get the User Id from Keycloak-server");
        List<UserRepresentation> userRepresentations = restTemplateClient.getKeyCloakUser(teacher.getEmcode().toLowerCase());
        UserRepresentation userRepresentation = userRepresentations.stream()
                .filter(users ->  users.getEmail().equals(teacher.getEmail().toLowerCase()))
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

    private TeacherResponse mapToTeacherResponse(Teacher teacher){
        return TeacherResponse.builder()
                .teacherId(teacher.getTeacherId())
                .fname(teacher.getFname())
                .emcode(teacher.getEmcode())
                .age(teacher.getAge())
                .lname(teacher.getLname())
                .email(teacher.getEmail())
                .address(teacher.getAddress())
                .department(teacher.getDepartment())
                .phonenumber(teacher.getPhonenumber())
                .password(teacher.getPassword())
                .branch(teacher.getBranch())
                .build();
    }


}









  


