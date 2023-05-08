package com.elearning.teacherservice.model;


import com.elearning.teacherservice.utils.EmployeeCodeGenerator;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.List;

import static com.elearning.teacherservice.model.Enum.ErrorConstants.ERROR_INVALID_ID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "htno")
    @GenericGenerator(
            name = "htno",
            strategy = "com.elearning.teacherservice.utils.EmployeeCodeGenerator",
            parameters = {
                    @Parameter(name = "optimizer", value = "pooled"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = EmployeeCodeGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = EmployeeCodeGenerator.VALUE_PREFIX_PARAMETER, value = "01K"),
                    @Parameter(name = EmployeeCodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d")
            })
    @Column(name = "emcode", length=1024)
    private String emcode;

//    @NotBlank(message = ERROR_INVALID_ID)
//    @NotEmpty(message = ERROR_INVALID_ID)
//    @NotNull(message = ERROR_INVALID_ID)
//    @NonNull
    @Column(name = "teacherId")
    private String teacherId;

    @Column(name = "fname")
    private String fname;

    @Column(name = "lname")
    private String lname;

    @Column(name = "email")
    private String email;

    @Column(name="age")
    private Integer age;

    @Column(name = "password")
    private String password;

    @Column(name = "phonenumber")
    private String phonenumber;



    @Column(name = "address")
    private String address;

    @Column(name = "department")
    private Integer department;

    @Column(name = "branch")
    private Integer branch;





}