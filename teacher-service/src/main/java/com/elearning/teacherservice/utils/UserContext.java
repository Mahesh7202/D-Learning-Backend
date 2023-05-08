package com.elearning.teacherservice.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
    public static final String USER_ID        = "tmx-user-id";
    public static final String KEYCLOAK_ID = "tmx-keycloak-id";

    private String correlationId= new String();
    private String authToken= new String();
    private String userId = new String();
    private String keycloakId = new String();


}
