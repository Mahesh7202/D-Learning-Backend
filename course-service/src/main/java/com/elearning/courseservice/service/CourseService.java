package com.elearning.courseservice.service;

import com.elearning.courseservice.dto.CourseRequest;
import com.elearning.courseservice.dto.CourseResponse;
import com.elearning.courseservice.dto.Resource;
import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.repository.CourseRepository;
import com.elearning.courseservice.service.client.RestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final RestTemplateClient restTemplateClient;


    public CourseResponse saveCourse(CourseRequest courseRequest){
        if(courseRequest == null){
            log.error("CourseRequest is empty: Request Body");
            return null;
        }
        log.info("Request to create Course of CourseName: {}",courseRequest.getCoursename());
        Course course = Course.builder()
                       .coursename(courseRequest.getCoursename())
                       .credits(courseRequest.getCredits())
                       .coursetype(courseRequest.getCoursetype())
                       .build();

        Course course1 = courseRepository.save(course);
        if(course1!=null){
            log.info("Creation Course of CourseName: {}",course1.getCoursename());
            log.info("[Resource-server] Sending request to Resource Server to create a Resource Space");
            Resource resource = Resource.builder()
                    .sucode(course.getSucode())
                    .build();

            Resource resource1 = restTemplateClient.createResource(resource);
            if(resource1!=null){
                log.info("Resource Space was created of sucode: {}",resource1.getSucode());
                return mapToCourseResponse(course1);
            }
        }
        log.error("Unable to create Course of CourseName: {}", course1.getCoursename());
        return null;
    }

    public CourseResponse findCourseById(String sucode){
        log.info("Retrieve Course by SUCODE: {}", sucode);
        Course course = courseRepository.findBySucode(sucode);
        if(course==null){
            log.error("Unable to find the Course with SUCODE: {}",sucode);
            return null;
        }
        log.info("Course was retrieve successful");
        return mapToCourseResponse(course);
    }

    public List<CourseResponse> findAllCourses(){
        log.info("Retrieve All Courses");
        List<Course> courses = courseRepository.findAll();
        if(courses==null){
            log.error("Unable to find the Courses");
            return null;
        }
        log.info("Retrieval of All Courses was Successful");
        return courses.stream().map(this::mapToCourseResponse).toList();
    }

    public List<CourseResponse> findCoursesByCourseType(Integer coursetype){
        log.info("Retrieve Courses by CourseType: {}", coursetype);
        List<Course> courses = courseRepository.findByCoursetype(coursetype);
        if(courses.isEmpty()){
            log.error("Unable to find the Courses with CourseType: {}",coursetype);
            return null;
        }
        log.info("Courses was retrieve successful");
        return courses.stream().map(this::mapToCourseResponse).toList();
    }


    public CourseResponse updateCourse(String sucode, CourseRequest courseRequest) {
        if (courseRequest == null) {
            log.error("Course Request is empty: Request Body");
            return null;
        }
        log.info("Updating Course of SUCODE: {}", sucode);
        Course course = courseRepository.findBySucode(sucode);
        if (course != null) {
            Course course1 = Course.builder()
                    .coursename(courseRequest.getCoursename())
                    .credits(courseRequest.getCredits())
                    .coursetype(courseRequest.getCoursetype())
                    .build();
            deleteCourseByCourseId(sucode);
            Course course2 = courseRepository.save(course1);
            if (course2 != null) {
                log.info("Creation Course of SUCODE: {}", sucode);
                return mapToCourseResponse(course2);
            }
        }
        log.error("Unable to update Course of SUCODE: {}", sucode);
        return null;
    }

//        Resource resource = restTemplateClient.getResourceBySucode(sucode);
//        resource.setSucode(course1.getSucode());
//        restTemplateClient.updateResource(sucode,resource);


    public void deleteCourseByCourseId(String sucode){
        log.info("Delete Course of SUCODE: {}",sucode);
        courseRepository.deleteById(sucode);
       // restTemplateClient.deleteResource(sucode);
        log.info("Deleted");
    }


    private CourseResponse mapToCourseResponse(Course course){
        return CourseResponse.builder()
                .sucode(course.getSucode())
                .coursename(course.getCoursename())
                .coursetype(course.getCoursetype())
                .credits(course.getCredits())
                .build();
    }

}
