package com.elearning.resourcesservice.repository;

import com.elearning.resourcesservice.model.SubjectResources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectResourceRepository extends JpaRepository<SubjectResources, Long> {
    SubjectResources findBySucode(String su_code);

    void deleteBySucode(String su_code);

    Boolean existsBySucode(String subject_code);
}
