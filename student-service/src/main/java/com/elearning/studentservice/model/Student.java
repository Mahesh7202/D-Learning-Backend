package com.elearning.studentservice.model;


import com.elearning.studentservice.utils.HallTKNoGenerator;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

import static com.elearning.studentservice.model.Enum.ErrorConstants.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "htno")
    @GenericGenerator(
            name = "htno",
            strategy = "com.elearning.studentservice.utils.HallTKNoGenerator",
            parameters = {
                    @Parameter(name = "optimizer", value = "pooled"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = HallTKNoGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = HallTKNoGenerator.VALUE_PREFIX_PARAMETER, value = "01K"),
                    @Parameter(name = HallTKNoGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d")
            })
//    @NotBlank(message = ERROR_INVALID_HALL_TICKET_ID)
//    @NotEmpty(message = ERROR_INVALID_HALL_TICKET_ID)
//    @NotNull(message = ERROR_INVALID_HALL_TICKET_ID)
//    @NonNull
    @Column(name = "htno",length = 1024)
    private String htno;


    @NotBlank(message = ERROR_INVALID_ID)
    @NotEmpty(message = ERROR_INVALID_ID)
    @NotNull(message = ERROR_INVALID_ID)
    @NonNull
    @Column(name = "studentId")
    private String studentId;


    @NotBlank(message = ERROR_INVALID_VALUE)
    @NotEmpty(message = ERROR_INVALID_VALUE)
    @NotNull(message = ERROR_INVALID_VALUE)
    @NonNull
    @Column(name = "fname")
    private String fname;

    @NotBlank(message = ERROR_INVALID_VALUE)
    @NotEmpty(message = ERROR_INVALID_VALUE)
    @NotNull(message = ERROR_INVALID_VALUE)
    @NonNull
    @Column(name = "lname")
    private String lname;

    @NotBlank(message = ERROR_INVALID_EMAIL)
    @NotEmpty(message = ERROR_INVALID_EMAIL)
    @NotNull(message = ERROR_INVALID_EMAIL)
    @Email(message = ERROR_INVALID_EMAIL)
    @Column(name = "email")
    private String email;

    @NotBlank(message = ERROR_INVALID_VALUE)
    @NotEmpty(message = ERROR_INVALID_VALUE)
    @NotNull(message = ERROR_INVALID_VALUE)
    @Length(message = ERROR_INVALID_VALUE, min=8)
    @NonNull
    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "branch")
    private Integer branch;

    @Column(name = "department")
    private Integer department;

    @Column(name = "semno")
    private Integer semno;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "age")
    private String age;

}