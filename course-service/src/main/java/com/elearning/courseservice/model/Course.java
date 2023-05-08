package com.elearning.courseservice.model;


import com.elearning.courseservice.util.SuCodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "course")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "su_code")
    @GenericGenerator(
            name = "su_code",
            strategy = "com.elearning.courseservice.util.SuCodeGenerator",
            parameters = {

                    @Parameter(name = "optimizer", value = "pooled"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = SuCodeGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = SuCodeGenerator.VALUE_PREFIX_PARAMETER, value = "01K"),
                    @Parameter(name = SuCodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d"),
            })
    @Column(name = "sucode",length = 1024)
    private String sucode;

    @Column(name = "coursename")
    private String coursename;

    @Column(name = "coursetype")
    private Integer coursetype;

    @Column(name = "credits")
    private Integer credits;


}
