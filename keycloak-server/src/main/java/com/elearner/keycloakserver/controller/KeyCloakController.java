package com.elearner.keycloakserver.controller;

import com.elearner.keycloakserver.DTO.User;
import com.elearner.keycloakserver.service.KeyCloakService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/auth/user")
public class KeyCloakController {

    private KeyCloakService service;


    @Autowired
    public KeyCloakController(KeyCloakService service) {
        this.service = service;
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public String addUser(@RequestBody User userDTO){
        log.info("Inside [/auth/user/addUser], request to create User");
        String user = (String) service.addUser(userDTO);
        if(user==null){
            log.error("Task of creation of User was Failed");
            return null;
        }
        log.info("Task of creation of User was Completed");
        return user;
    }


    @RolesAllowed("ADMIN")
    @GetMapping(path = "/{userName}")
    public ResponseEntity<?> getUser(@PathVariable("userName") String userName){
        log.info("Inside [/auth/user/getUser], request to Retrieve User");
        List<UserRepresentation> user = service.getUser(userName);
        if(user==null){
            log.error("Task to get User by UserName: {} was Failed",userName);
            return null;
        }
        log.error("Task of get User by UserName: {} was Completed",userName);
        return ResponseEntity.ok().body(user);
    }


    //@RolesAllowed({"ADMIN","TEACHER","auth/user"})
    @PutMapping(path = "/update/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @RequestBody User userDTO){
        log.info("Inside [/auth/user/updateUser], request to update User");
        String user = (String) service.updateUser(userId, userDTO);
        if(user==null){
            log.error("Task to update User by UserId: {} was Failed",userId);
            return null;
        }
        log.error("Task of update User by UserId: {} was Completed",userId);
        return user;
    }


    @RolesAllowed("ADMIN")
    @DeleteMapping(path = "/delete/{userId}")
    public Boolean deleteUser(@PathVariable("userId") String userId){
        log.info("Inside [/auth/user/deleteUser], request to delete User");
        Boolean status = service.deleteUser(userId);
        if(!status){
            log.error("Task to delete User by UserId: {} was Failed",userId);
            return null;
        }
        log.error("Task of delete User by UserId: {} was Completed",userId);
        return true;
    }


    @RolesAllowed({"ADMIN","TEACHER","auth/user"})
    @GetMapping(path = "/verification-link/{userId}")
    public String sendVerificationLink(@PathVariable("userId") String userId){
        log.info("Inside [/auth/user/createauth/user], request to create auth/user");
        service.sendVerificationLink(userId);
        return "Verification Link Send to Registered E-mail Id.";
    }


    @GetMapping(path = "/reset-password/{userId}")
    @RolesAllowed({"ADMIN","TEACHER","auth/user"})
    public String sendResetPassword(@PathVariable("userId") String userId){
        log.info("Inside [/auth/user/createauth/user], request to create auth/user");
        service.sendResetPassword(userId);
        return "Reset Password Link Send Successfully to Registered E-mail Id.";
    }
}
