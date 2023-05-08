package com.elearning.teacherservice.exception;

import lombok.Data;

@Data
public class UserNotCreated extends RuntimeException{

    private static final long serialVersionUID = 1904585489531578456L;

    public UserNotCreated(String message) {
        super(message);
    }
}

