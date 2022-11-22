package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.shop.gateway.dto.UserDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;


    public UserDto getUserDto(String userId) {
        UserRepresentation ur = keycloak
                .realm(realm)
                .users()
                .get(userId)
                .toRepresentation();
        return mapperRepresentationToDto(ur);
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm).users().delete(userId);
    }

    public List<String> getRoles() {
        return keycloak
                .realm(realm)
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .filter(s -> s.matches("^[A-Z]+"))
                .toList();
    }

    public String getRole(String userId) {
        List<RoleRepresentation> list = keycloak
                .realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .listAll();
        return list.get(0).getName();
    }

    public List<UserDto> getUserDtoList() {
        List<UserRepresentation> userRepresentations = keycloak
                .realm(realm)
                .users()
                .list();

        List<UserDto> userDtoList = new ArrayList<>();
        userRepresentations.forEach(ur -> userDtoList.add(mapperRepresentationToDto(ur)));
        return userDtoList;
    }

    private UserDto mapperRepresentationToDto(UserRepresentation ur) {
        return UserDto.builder()
                .id(ur.getId())
                .email(ur.getEmail())
                .firstname(ur.getFirstName())
                .lastname(ur.getLastName())
                .username(ur.getUsername())
                .registerDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(ur.getCreatedTimestamp()), TimeZone
                        .getDefault().toZoneId()))
                .role(getRole(ur.getId()))
                .build();
    }
}
