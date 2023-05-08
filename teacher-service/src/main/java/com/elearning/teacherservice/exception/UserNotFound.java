package com.elearning.teacherservice.exception;

import lombok.Data;

@Data
public class UserNotFound extends RuntimeException{

    private static final long serialVersionUID = 1904585489531578456L;

    public UserNotFound(String message) {
        super(message);
    }
}

