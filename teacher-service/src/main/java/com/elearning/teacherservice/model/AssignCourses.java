package com.elearning.teacherservice.model;

import com.elearning.teacherservice.utils.ListConvertor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "AssignCourses")
public class AssignCourses implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String teacherId;

    @Column(name="emcode")
    private String emcode;

    @Column(name = "courses")
    @Convert(converter = ListConvertor.class)
    private List<String> courses;

}
