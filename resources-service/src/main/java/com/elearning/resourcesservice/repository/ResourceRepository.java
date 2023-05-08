package com.elearning.resourcesservice.repository;


import com.elearning.resourcesservice.dto.ResourcesDto;
import com.elearning.resourcesservice.model.Resource;
import com.elearning.resourcesservice.model.SubjectResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

}
