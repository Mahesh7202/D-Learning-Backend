package com.elearning.studentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
    private String studentId;
    private String fname;
    private String lname;
    private String htno;
    private String email;
    private String password;

    private String address;
    private Integer branch;

    private Integer department;
    private Integer semno;

    private String age;
    private String phonenumber;
    private String city;




}
