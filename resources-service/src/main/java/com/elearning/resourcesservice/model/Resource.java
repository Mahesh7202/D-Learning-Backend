package com.elearning.resourcesservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "Resource")
public class Resource {

    @Id
    @Column(name = "resourceid")
    private String resourceid;

    @Column(name = "resourcetype")
    private String resourcetype;

    @Column(name = "resourcelength")
    private String resourcelength;
    @Column(name = "resourceURL")
    private String resourceURL;

    @Column(name = "resourcename")
    private String resourcename;

}
