package com.elearning.courseservice.service.client;

import com.elearning.courseservice.dto.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Component
public class RestTemplateClient {
        @Autowired
        RestTemplate restTemplate;


        @Value("${resource-base-url}")
        private String resourceBaseUrl;

        public Resource createResource(Resource resource){
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<Resource> userHttpEntity = new HttpEntity<Resource>(resource,headers);
                ResponseEntity<Resource> restExchange = restTemplate.exchange(resourceBaseUrl,
                        HttpMethod.POST, userHttpEntity, Resource.class);
                return restExchange.getBody();
        }


        public Resource updateResource(String sucode,Resource resource){
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<Resource> entity = new HttpEntity<Resource>(resource,headers);

                ResponseEntity<Resource> responseEntity = restTemplate.exchange(resourceBaseUrl+"/update/"+sucode,
                        HttpMethod.PUT, entity, Resource.class);
                return responseEntity.getBody();

        }

        public List<Resource> getResource(){
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<String> entity = new HttpEntity<String>(headers);

                ResponseEntity<List<Resource>> responseEntity = restTemplate.exchange(resourceBaseUrl,
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Resource>>(){});
                return responseEntity.getBody();
        }

        public void deleteResource(String sucode){
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<Resource> entity = new HttpEntity<Resource>(headers);

                restTemplate.exchange(resourceBaseUrl+"/delete/"+sucode,
                        HttpMethod.DELETE, entity, Void.class);
        }


}
