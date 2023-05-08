package com.elearner.keycloakserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Getter
@Setter
public class User {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
    private String role;
}
