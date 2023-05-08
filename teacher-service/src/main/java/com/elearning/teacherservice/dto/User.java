package com.elearning.teacherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
    private String role;
}
