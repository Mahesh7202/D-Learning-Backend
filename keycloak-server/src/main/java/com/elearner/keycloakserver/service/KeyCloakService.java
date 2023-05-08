package com.elearner.keycloakserver.service;

import com.elearner.keycloakserver.DTO.User;
import com.elearner.keycloakserver.config.Credentials;
import com.elearner.keycloakserver.config.KeycloakConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.elearner.keycloakserver.config.KeycloakConfig.clientId;
import static com.elearner.keycloakserver.config.KeycloakConfig.realm;


@AllArgsConstructor
@Service
@Slf4j
public class KeyCloakService {

    private final Keycloak keyCloak = KeycloakConfig.getInstance();

    public <T> Object addUser(User userDTO){
        if(userDTO == null){
            log.error("UserDTo Request is empty: Request Body");
            return null;
        }
        log.info("Request to create User of Username: {}",userDTO.getUserName());

        log.info("Instantiating of Required Credentials");
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        if(credential!=null){
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmailId());
            user.setCredentials(Collections.singletonList(credential));
            user.setEnabled(true);
            log.info("Request an Initial Instance From Keycloak");
            UsersResource instance = getInstance();
            if(instance!=null){
                Response response = instance.create(user);
                if(response!=null){
                    log.info("User instance was created and Specifying the role to an User");
                    addRealmRole(clientId+"-"+userDTO.getRole());
                    addClientRole(userDTO.getRole());
                    makeCompositionOfClientRole(clientId+"-"+userDTO.getRole(), userDTO.getRole());
                    addRealmRoleToUser(userDTO.getUserName().toLowerCase(),clientId+"-"+userDTO.getRole());
                    log.info("Added the Specific Role of an User");
                    return "success";
                }
            }
        }
        log.info("Unable to create User of Username: {}"+userDTO.getUserName());
        return null;
    }

    public List<UserRepresentation> getUser(String userName){
        log.info("Retrieve Users by UserName: {}", userName);
        log.info("Request an Initial Instance From Keycloak");
        UsersResource usersResource = getInstance();
        if(usersResource!=null){
            List<UserRepresentation> userRepresentations = usersResource.search(userName, true);
            if(!userRepresentations.isEmpty()){
                log.info("User retrieve was successful: {}",userRepresentations.isEmpty());
                return userRepresentations;
            }
        }
        log.error("Unable to find the Users with UserName: {}",userName);
        return null;
    }

    public <T>Object updateUser(String userId, User userDTO){
        if(userDTO == null){
            log.error("UserDTo Request is empty: Request Body");
            return null;
        }
        log.info("Request to Update User of Username: {}",userDTO.getUserName());
        log.info("Instantiating of Required Credentials");
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        if(credential!=null) {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmailId());
            user.setCredentials(Collections.singletonList(credential));
            log.info("Request an Initial Instance From Keycloak");
            UsersResource usersResource = getInstance();
            if(usersResource!=null){
                usersResource.get(userId).update(user);
                log.info("User instance was updated of UserID:{} and Specifying the role to an User",userId);
                addRealmRole(clientId + "-" + userDTO.getRole());
                addClientRole(userDTO.getRole());
                makeCompositionOfClientRole(clientId + "-" + userDTO.getRole(), userDTO.getRole());
                addRealmRoleToUser(userDTO.getUserName().toLowerCase(), clientId + "-" + userDTO.getRole());
                log.info("Updated the Specific Role of an User");
                return "updated";
            }
        }
        log.info("Unable to Update User of Username: {}"+userDTO.getUserName());
        return null;
    }
    public Boolean deleteUser(String userId){
        log.info("Delete User by UserID: {}",userId);
        UsersResource usersResource = getInstance();
        if(usersResource!=null){
            usersResource.get(userId)
                    .remove();
            log.info("User deleted of UserID: {}",userId);
            return true;
        }
        log.error("Unable to delete the User of UserId: {}",userId);
        return false;
    }


    public void sendVerificationLink(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .sendVerifyEmail();
    }

    public void sendResetPassword(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    public UsersResource getInstance(){
        return keyCloak.realm(realm).users();
    }



    private void addRealmRole(String new_role_name){
        log.info("Creation of Realm Role: {}",new_role_name);
        if(!getAllRealmRoles().contains(new_role_name)){
            log.info("Role: {} is not Available",new_role_name);
            RoleRepresentation roleRep = new RoleRepresentation();
            roleRep.setName(new_role_name);
            roleRep.setDescription("role_" + new_role_name);
            keyCloak.realm(realm).roles().create(roleRep);
            log.info("RealmRole: {} is Created",new_role_name);
        }
    }

    private List<String> getAllRealmRoles(){
        log.info("Request the All Realm Roles");
        List<String> availableRoles = keyCloak
                .realm(realm)
                .roles()
                .list()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
        return availableRoles;
    }

    private void addClientRole(String new_role_name){
        log.info("Creation of Client Role: {}",new_role_name);
        if(!getAllClientRoles().contains(new_role_name)){
            log.info("Role: {} is not Available",new_role_name);
            RoleRepresentation roleRep = new  RoleRepresentation();
            roleRep.setName(new_role_name);
            roleRep.setDescription("role_" + new_role_name);
            ClientRepresentation clientRep = keyCloak
                    .realm(realm)
                    .clients()
                    .findByClientId(clientId)
                    .get(0);
            keyCloak.realm(realm)
                    .clients()
                    .get(clientRep.getId())
                    .roles()
                    .create(roleRep);
            log.info("ClientRole: {} is Created",new_role_name);
        }
    }

    private List<String> getAllClientRoles(){
        log.info("Request the All Client Roles");
        ClientRepresentation clientRep = keyCloak
                .realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);
        List<String> availableRoles = keyCloak
                .realm(realm)
                .clients()
                .get(clientRep.getId())
                .roles()
                .list()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
        return availableRoles;
    }


    private void makeCompositeOfRelamRoles(String role_name){
        RoleRepresentation role = keyCloak
                .realm(realm)
                .roles()
                .get(role_name)
                .toRepresentation();
        List<RoleRepresentation> composites = new LinkedList<>();
        composites.add(keyCloak
                .realm(realm)
                .roles()
                .get("offline_access")
                .toRepresentation()
        );
        keyCloak.realm(realm).rolesById()
                .addComposites(role.getId(), composites);
    }

    private void makeCompositionOfClientRole(String role_name,String client_role){
        log.info("Composition of ClientRole: {} with role: {}",client_role,role_name);
        ClientRepresentation clientRep = keyCloak
                .realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);

        List<RoleRepresentation> compositeser = new LinkedList<>();
        compositeser.add(keyCloak
                .realm(realm)
                .clients()
                .get(clientRep.getId())
                .roles()
                .get(client_role)
                .toRepresentation()
        );

        keyCloak.realm(realm).roles().get(role_name).addComposites(compositeser);
    }


    private void addRealmRoleToUser(String userName, String role_name){
        log.info("Adding Realm Role to User:{}",userName);
        String userId = keyCloak
                .realm(realm)
                .users()
                .search(userName)
                .get(0)
                .getId();
        UserResource user = keyCloak
                .realm(realm)
                .users()
                .get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(keyCloak
                .realm(realm)
                .roles()
                .get(role_name)
                .toRepresentation()
        );
        user.roles().realmLevel().add(roleToAdd);
    }


}
