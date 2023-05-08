package com.elearning.teacherservice.exception;

        import lombok.Data;

@Data
public class RestException extends RuntimeException{

    private static final long serialVersionUID = 1904585489531578456L;

    public RestException(String message) {
        super(message);
    }
}

