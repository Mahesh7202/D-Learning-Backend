package com.elearning.courseservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private String sucode;
    private String coursename;
    private Integer credits;
    private Integer coursetype;
}
