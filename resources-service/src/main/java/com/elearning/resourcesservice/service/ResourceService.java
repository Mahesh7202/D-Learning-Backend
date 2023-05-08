package com.elearning.resourcesservice.service;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.elearning.resourcesservice.buckets.BucketName;
import com.elearning.resourcesservice.dto.ResourcesDto;
import com.elearning.resourcesservice.filestore.FileStore;
import com.elearning.resourcesservice.model.Resource;
import com.elearning.resourcesservice.model.SubjectResources;
import com.elearning.resourcesservice.repository.ResourceRepository;
import com.elearning.resourcesservice.repository.SubjectResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {

    private final SubjectResourceRepository subjectResourceRepository;

    private final ResourceRepository resourceRepository;

    private final FileStore fileStore;


    public ResourcesDto save(ResourcesDto resourcesDto){
        if(resourcesDto == null){
            log.error("Request Body: ResourceDto is Empty");
        }
        log.info("Request to Create Resource of SUCODE",resourcesDto.getSucode());
        SubjectResources subjectResources = SubjectResources.builder()
                .sucode(resourcesDto.getSucode())
                .build();
        SubjectResources subjectResources1 = subjectResourceRepository.save(subjectResources);
        if(subjectResources1 == null){
            log.error("Cannot able to create Resource for SUCODE: {}",subjectResources1.getSucode());
            return null;
        }
        log.info("Creation of Student of Student Id: {}",subjectResources1.getSucode());
        return mapToStudentResponse(subjectResources1);
    }

    public ResourcesDto findBySubjectCode(String sucode){
        log.info("Retrieve Resource by SUCODE: {}", sucode);
        SubjectResources subjectResources = subjectResourceRepository.findBySucode(sucode);
        if(subjectResources==null){
            log.error("Unable to find the Resource with SUCODE: {}",sucode);
            return null;
        }
        log.info("Resource was retrieve successful");
        return mapToStudentResponse(subjectResources);
    }

    public List<ResourcesDto> findAllSubjects(){
        log.info("Retrieve All Resources");
        List<SubjectResources> subjectResources = subjectResourceRepository.findAll();
        if(subjectResources==null){
            log.error("Unable to find the Resources");
            return null;
        }
        log.info("Retrieval of All Resources was Successful");
        return subjectResources.stream().map(this::mapToStudentResponse).toList();
    }

    public ResourcesDto updateSubjectResource(String sucode, ResourcesDto resourcesDto){
        if(resourcesDto == null){
            log.error("ResourceDto Request is empty: Request Body");
            return null;
        }
        log.info("Updating Resource of SUCODE: {}",sucode);
        SubjectResources subjectResources = subjectResourceRepository.findBySucode(sucode);
        if(subjectResources!=null){
             subjectResources.setResource(resourcesDto.getResource());
             subjectResources.setSucode(resourcesDto.getSucode());
             SubjectResources subjectResources1 = subjectResourceRepository.save(subjectResources);
            if(subjectResources1!=null) {
                log.info("Updated Resource of SUCODE: {}",sucode);
                return mapToStudentResponse(subjectResources1);
            }
        }
        log.error("Cannot able to update Resource for SUCODE: {}",sucode);
        return null;
    }

    public ResourcesDto addResourceToSucode(String sucode, Resource resource){
        log.info("Add ResourceFile to the Resource of SUCODE: {}",sucode);
        SubjectResources subjectResources = subjectResourceRepository.findBySucode(sucode);
        if(subjectResources!=null){
            subjectResources.getResource().add(resource);
            SubjectResources subjectResources1 = subjectResourceRepository.save(subjectResources);
            if(subjectResources1!=null){
                log.info("ResourceFile is added to Resource of SUCODE: {}",sucode);
                return mapToStudentResponse(subjectResources);
            }
        }
        log.error("Cannot able to Add ResourceFile Resource for SUCODE: {}",sucode);
        return null;
    }

    public PutObjectResult uploadResource(String sucode, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            log.error("Request to Upload File is Empty");
            throw new IllegalStateException("Cannot upload empty file ["+file.getSize()+"]");
        }
        log.info("Request to Upload the File to Resource of SUCODE: {}",sucode);
        if(subjectResourceRepository.existsBySucode(sucode)){

            String resourceid = UUID.randomUUID().toString();
            String url = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), resourceid);
            String file_Size = String.valueOf(file.getSize());
            String file_type = file.getContentType();
            String filename = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());


            Resource resource = Resource.builder()
                    .resourceid(resourceid)
                    .resourcename(filename)
                    .resourcelength(file_Size)
                    .resourcetype(file_type)
                    .resourceURL(url)
                    .build();

            ResourcesDto resourcesDto = addResourceToSucode(sucode,resource);
            if(resourcesDto!=null){
                log.info("Initialize the meta data");
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", file_type);
                metadata.put("Content-Length", file_type);


                try {
                    log.info("Request to save File into AWS Cloud");
                    PutObjectResult putObjectResult = fileStore.save(url, filename, Optional.of(metadata), file.getInputStream());
                    if(putObjectResult!=null){
                        log.info("File is saved in AWS Cloud Successfully");
                        return putObjectResult;
                    }
                }catch (IOException e){
                    throw new IllegalStateException(e);
                }
            }
            log.error("Unable to Save File into AWS Cloud");
            return null;
        }
        log.error("Unable to Upload the File to Resource of SUCODE: {}",sucode);
        return null;
    }



    public byte[] downloadResource(String sucode, String resource_id) {
        log.info("Download the Resource File of Resource SUCODE: {}",sucode);
        ResourcesDto resourcesDto = findBySubjectCode(sucode);
        if(resourcesDto!=null){
            Resource resourc = resourcesDto.getResource().stream().filter(resource -> resource.getResourceid().equals(resource_id))
                    .findFirst()
                    .orElseThrow(()->new IllegalStateException(String.format("Resource of following %s of resourceId %s not found",sucode,resource_id)));
            String path = String.valueOf(resourc.getResourcename()); //String.format("%s/%s",BucketName.PROFILE_IMAGE.getBucketName(),resource_id);
            System.out.println(path+"pathhhh");


            return fileStore.download(resourc.getResourceURL(),resourc.getResourcename());

        }
        log.error("Unable to Download the Resource File of SUCODE: {}",sucode);
        return null;
    }

    public void deleteResource(String sucode,String resource_id){
        log.info("Delete the ResourceFile for Resource of SUCODE: {}",sucode);
        ResourcesDto resourcesDto = findBySubjectCode(sucode);
        if(resourcesDto!=null){
            log.info("Finding the ResourceFile of ResourceId: {}",resource_id);
            Resource resourc = resourcesDto.getResource().stream().filter(resource -> resource.getResourceid().equals(resource_id))
                    .findFirst()
                    .orElseThrow(()->new IllegalStateException(String.format("Resource of following %s of resourceId %s not found",sucode,resource_id)));
            if(resourc!=null){
                fileStore.delete(BucketName.PROFILE_IMAGE.getBucketName(), resourc.getResourcename());
                log.info("ResourceFile of ResourceId: {} is Deleted",resource_id);
            }
        }

        log.error("Unable to Delete ResourceFile for Resource of SUCODE: {}",sucode);
    }

    @Transactional
    public void deleteSubjectResource(String sucode) {
        log.info("Delete Resource of SUCODE: {}",sucode);
        ResourcesDto resourcesDto = findBySubjectCode(sucode);
        if(resourcesDto!=null){
            List<Resource> resources = resourcesDto.getResource();
            if(resources!=null){
                resources.stream().map(resource -> {deleteResource(sucode,resource.getResourceid());
                    return true;
                });
            }
            subjectResourceRepository.deleteBySucode(sucode);
            log.info("Deleted");
        }
        log.error("Unable to Delete Resource of SUCODE: {}",sucode);
    }


    private ResourcesDto mapToStudentResponse(SubjectResources subjectResources){
        return ResourcesDto.builder()
                .resource(subjectResources.getResource())
                .sucode(subjectResources.getSucode())
                .build();
    }





}
