package com.elearning.teacherservice.service;

import com.elearning.teacherservice.dto.AssignCoursesReq;
import com.elearning.teacherservice.exception.RestException;
import com.elearning.teacherservice.model.AssignCourses;
import com.elearning.teacherservice.repository.AssignCoursesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.elearning.teacherservice.model.Enum.ErrorConstants.ERROR_DELETING_USER;
import static com.elearning.teacherservice.model.Enum.ErrorConstants.ERROR_NO_BODY;


@Service
@RequiredArgsConstructor
@Slf4j
public class AssignCoursesService {

        private final AssignCoursesRepository assignCoursesRepository;

        public AssignCoursesReq saveAssignCourses(AssignCoursesReq assignCoursesReq){
            if(assignCoursesReq == null){
                log.error("Student Request is empty: Request Body");
                throw new RestException(ERROR_NO_BODY);
            }
            log.info("Request to create Student by AssignCoursesId: {}",assignCoursesReq.getEmcode());
            AssignCourses assignCourses = AssignCourses.builder()
                    .teacherId(assignCoursesReq.getTeacherId())
                    .emcode(assignCoursesReq.getEmcode())
                    .courses(assignCoursesReq.getCourses())
                    .build();

            AssignCourses save = assignCoursesRepository.save(assignCourses);
            if(save != null){
                log.info("Creation of Student of Student Id: {}",save.getEmcode());
                return mapToAssignCoursesReq(save);
            }
            log.error("Unable to create Student for AssignCoursesId: {}",assignCourses.getEmcode());
            return null;
        }


        public AssignCoursesReq findAssignCoursesByEmcode(String emcode) {
            log.info("Retrieve AssignCourses by Emcode No: {}", emcode);
            AssignCourses assignCourses = assignCoursesRepository.findByEmcode(emcode);
            if(assignCourses==null){
                log.error("Unable to find the AssignCourses with Emcode No: {}",emcode);
                return null;
            }
            log.info("AssignCourses was retrieve successful");
            return mapToAssignCoursesReq(assignCourses);
        }

        public List<AssignCoursesReq> findAllAssignCourses(){

            log.info("Retrieve All AssignCourses");
            List<AssignCourses> assignCoursess = assignCoursesRepository.findAll();
            if(assignCoursess.isEmpty()){
                log.error("Unable to find the AssignCourses");
                return null;
            }
            log.info("Retrieval of All AssignCourses was Successful");
            return assignCoursess.stream().map(this::mapToAssignCoursesReq).collect(Collectors.toList());
        }


        public AssignCoursesReq updateAssignCourses(String teacherId, AssignCoursesReq assignCoursesReq){
            if(assignCoursesReq == null){
                log.error("Student Request is empty: Request Body");
                throw new RestException(ERROR_NO_BODY);
            }
            log.info("Updating Student of AssignCoursesId: {}",teacherId);
            AssignCourses assignCourses = assignCoursesRepository.findByTeacherId(teacherId);
            log.info("Updating Student of AssignCoursesId: {}",assignCoursesReq.getCourses().isEmpty());
            assignCourses.setCourses(assignCoursesReq.getCourses());


            AssignCourses assignCourses1 = assignCoursesRepository.save(assignCourses);
            if(assignCourses1==null){
                log.error("Cannot able to update Student for AssignCoursesId: {}",assignCourses1.getTeacherId());
                return null;
            }
            log.info("Updated Student of Student Id: {}",assignCourses1.getTeacherId());
            return mapToAssignCoursesReq(assignCourses1);
        }

        @Transactional
        public void deleteAssignCoursesByEmcode(String emcode){
            log.info("Delete Student of AssignCoursesId: {}",emcode);
            try {
                assignCoursesRepository.deleteByEmcode(emcode);
            }catch(Exception e){
                log.info("Deletion of User for userId: {} was failed",emcode);
                throw new RestException(ERROR_DELETING_USER);
            }
        }


        private AssignCoursesReq mapToAssignCoursesReq(AssignCourses assignCourses){
            return AssignCoursesReq.builder()
                    .emcode(assignCourses.getEmcode())
                    .courses(assignCourses.getCourses())
                    .build();
        }


}
