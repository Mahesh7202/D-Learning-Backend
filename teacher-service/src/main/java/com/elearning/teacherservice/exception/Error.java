package com.elearning.teacherservice.exception;

import com.elearning.teacherservice.model.Enum.Category;
import com.elearning.teacherservice.model.Enum.Code;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Error {

    private Category category;
    private Code code;
    private String message;
    private Date timeStamp;

}
