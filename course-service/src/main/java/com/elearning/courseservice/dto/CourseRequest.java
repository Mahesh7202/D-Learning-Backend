package com.elearning.courseservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    private String coursename;
    private Integer credits;
    private Integer coursetype;

}
