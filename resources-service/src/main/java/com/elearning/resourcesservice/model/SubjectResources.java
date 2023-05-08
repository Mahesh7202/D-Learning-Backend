package com.elearning.resourcesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "SubjectResources")
public class SubjectResources {

    @Id
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long Id;

    @Column(name = "sucode")
    private String sucode;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "resources")
    private List<Resource> resource = new ArrayList<>();

    public SubjectResources(String s) {
        this.sucode = s;
    }
}
