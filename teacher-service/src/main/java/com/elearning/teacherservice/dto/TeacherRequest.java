package com.elearning.teacherservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherRequest {
    private String teacherId;
    private String emcode;
    private String fname;
    private String lname;

    private Integer age;
    private String email;
    private String password;

    private String address;
    private String phonenumber;
    private Integer department;
    private Integer branch;

}
