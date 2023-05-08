package com.elearning.resourcesservice.controller;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.elearning.resourcesservice.dto.ResourcesDto;
import com.elearning.resourcesservice.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "resource")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createResource(@RequestBody ResourcesDto resourcesDto){
        log.info("Inside [/Resource/createResource], request to create Resource");
        ResourcesDto resourcesDto1 = resourceService.save(resourcesDto);
        if(resourcesDto1==null){
            log.error("Task of creation of Resource was Failed");
            return null;
        }
        log.info("Task of creation of Resource was Completed");
        return ResponseEntity.ok().body(resourcesDto1);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllResources(){Inside [/Resource/getAllResources], request to get All Resource");
        List<ResourcesDto> resourcesDtos = res
        log.info("ourceService.findAllSubjects();
        if(resourcesDtos==null){
            log.error("Task to get All Resource Failed");
            return null;
        }
        log.error("Task of get All Resource Completed");
        return ResponseEntity.ok().body(resourcesDtos);
    }

    @GetMapping("{sucode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getResourceBySuCode(@PathVariable("sucode") String sucode){
        log.info("Inside [/Resource/getResourceBySuCode], request to get Resource by SUCODE");
        ResourcesDto resourcesDto = resourceService.findBySubjectCode(sucode);
        if(resourcesDto==null){
            log.error("Task to get Resource by SUCODE: {} is Failed",sucode);
            return null;
        }
        log.info("Task to get Resource by SUCODE: {} is Completed",sucode);
        return ResponseEntity.ok().body(resourcesDto);
    }

    @PutMapping("{sucode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> UpdateResourceBySuCode(@PathVariable("sucode") String sucode, @RequestBody ResourcesDto resourcesDto){
        log.info("Inside [/Resource/UpdateResourceBySuCode], request to update Resource by SUCODE");
        ResourcesDto resourcesDto1 = resourceService.updateSubjectResource(sucode,resourcesDto);
        if(resourcesDto1==null){
            log.error("Task to Update Resource by SUCODE: {} is Failed",sucode);
            return null;
        }
        log.info("Task to get Resource by SUCODE: {} is Completed",sucode);
        return ResponseEntity.ok().body(resourcesDto1);
    }

    @PostMapping(
            path = "{sucode}/resource/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadResource(@PathVariable("sucode") String sucode, @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Inside [/Resource/uploadResource], request to Upload File of Resource to Cloud AWS");
        PutObjectResult putObjectResult = resourceService.uploadResource(sucode, file);
        if(putObjectResult==null){
            log.error("Task to Upload ResourceFile into AWS Cloud is Failed");
            return null;
        }
        log.info("Task to Upload ResourceFile into AWS Cloud is Completed");
        return ResponseEntity.ok().body(putObjectResult);
    }

    @GetMapping(value = "{sucode}/{resourceid}/download")
    public ResponseEntity<?> downloadResource(@PathVariable("sucode") String sucode, @PathVariable("resourceid") String resourceid){
        log.info("Inside [/Resource/downloadResource], request to Download ResourceFile");
        byte[] bytes = resourceService.downloadResource(sucode,resourceid);
        if(bytes==null){
            log.error("Task to Download ResourceFile From AWS Cloud of ResourceId:{} for Resource of SUCODE:{} is Failed",resourceid,sucode);
            return null;
        }
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
        log.info("Task to Download ResourceFile From AWS Cloud of ResourceId:{} for Resource of SUCODE:{} is Completed",resourceid,sucode);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(byteArrayResource);
    }

    @DeleteMapping("{sucode}")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed
    public void deleteResourceBySuCode(@PathVariable("sucode") String sucode){
        log.info("Inside [/Resource/deleteResourceBySuCode], request to delete Resource by SUCODE");
        resourceService.deleteSubjectResource(sucode);
        log.info("Task to Delete Resource for SUCODE: {} was Completed",sucode);
    }

    @DeleteMapping("{sucode}/{resourceid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteResourceBySuCode(@PathVariable("sucode") String sucode,@PathVariable("resourceid") String resourceid){
        log.info("Inside [/Resource/deleteResourceBySuCode], request to delete ResourceFile from Resource");
        resourceService.deleteResource(sucode,resourceid);
        log.info("Task to Delete ResourceFile of ResourceId for Resource of SUCODE: {} was Completed",resourceid,sucode);
    }

}
