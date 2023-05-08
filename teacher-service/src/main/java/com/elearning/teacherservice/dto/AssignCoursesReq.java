package com.elearning.teacherservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignCoursesReq {
    private String teacherId;
    private String emcode;
    private List<String> courses;
}
