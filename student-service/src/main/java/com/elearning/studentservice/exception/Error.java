package com.elearning.studentservice.exception;

import com.elearning.studentservice.model.Enum.Category;
import com.elearning.studentservice.model.Enum.Code;
import lombok.*;

import java.util.Date;

@Data
@Builder
public class Error {

    private Category category;
    private Code code;
    private String message;
    private Date timeStamp;

}
