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
       return keycloak
               .realm(realm)
               .users()
               .get(userId)
               .roles()
               .realmLevel()
               .listAll().get(0)
               .getName();
    }

    public List<UserDto> getUserDtoList() {
        List<UserRepresentation> userRepresentations = keycloak
                .realm(realm)
                .users()
                .list();

        List<UserDto> userDtoList = new ArrayList<>();
        userRepresentations.forEach(u -> {
            UserDto userDto = UserDto.builder()
                    .email(u.getEmail())
                    .firstname(u.getFirstName())
                    .lastname(u.getLastName())
                    .username(u.getUsername())
                    .registerDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(u.getCreatedTimestamp()), TimeZone
                            .getDefault().toZoneId()))
                    .role(getRole(u.getId()))
                    .build();
            userDtoList.add(userDto);
        });
        return userDtoList;
    }

}
