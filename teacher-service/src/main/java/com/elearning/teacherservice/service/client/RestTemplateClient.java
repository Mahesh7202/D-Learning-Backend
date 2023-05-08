package com.elearning.teacherservice.service.client;

import com.elearning.teacherservice.dto.User;
import org.keycloak.representations.idm.UserRepresentation;
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


    @Value("${key-cloak-base-url}")
    private String keyCloakBaseUrl;

    public String createKeyCloakUser(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> userHttpEntity = new HttpEntity<User>(user,headers);
        ResponseEntity<String> restExchange = restTemplate.exchange(keyCloakBaseUrl,
                HttpMethod.POST, userHttpEntity, String.class);
        return restExchange.getBody();
    }


    public String updateKeyCloakUser(String userId, User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(keyCloakBaseUrl+"/update/"+userId,
                HttpMethod.PUT, entity, String.class);
        return responseEntity.getBody();

    }

    public List<UserRepresentation> getKeyCloakUser(String userName){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<List<UserRepresentation>> responseEntity = restTemplate.exchange(keyCloakBaseUrl+"/"+userName,
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<UserRepresentation>>(){});
        return responseEntity.getBody();
    }

    public Boolean deleteKeyCloakUser(String userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<User>(headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(keyCloakBaseUrl+"/delete/"+userId,
                HttpMethod.DELETE, entity, Boolean.class);
        return responseEntity.getBody();
    }


}
