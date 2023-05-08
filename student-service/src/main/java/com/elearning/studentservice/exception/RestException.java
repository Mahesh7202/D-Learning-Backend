package com.elearning.studentservice.exception;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
public class RestException extends RuntimeException{

    private static final long serialVersionUID = 1904585489531578456L;

    public RestException(String message) {
        super(message);
    }
}

