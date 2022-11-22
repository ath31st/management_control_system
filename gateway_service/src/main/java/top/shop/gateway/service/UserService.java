package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.entity.User;
import top.shop.gateway.util.Role;

import javax.ws.rs.client.ClientBuilder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    private final Keycloak keycloak;

    public List<String> getRoles() {
        return keycloak
                .realm(realm)
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
    }


    public List<String> getAllRoles() {
        ClientRepresentation clientRep = keycloak
                .realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);
        List<String> availableRoles = keycloak
                .realm(realm)
                .clients()
                .get(clientRep.getId())
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        return availableRoles;
    }

//    public User getUserByUsername(String username) {
//        return userRepository.getUser(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//    public void registerUser(UserDto userDto) {
//        if (userRepository.getUser(userDto.getUsername()).isPresent() && userDto.getRole().equals(Role.ROLE_ADMINISTRATOR.name()))
//            return;
//        if (userRepository.getUser(userDto.getUsername()).isPresent())
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists!");
//
//        User user = User.builder()
//                .firstname(userDto.getFirstname())
//                .lastname(userDto.getLastname())
//                .username(userDto.getUsername())
//                .email(userDto.getEmail())
//                .shopServiceName(Value.DEFAULT.name()) //TODO administrator must change this field
//                .shopUrl(Value.DEFAULT.name()) //TODO administrator must change this field
//                .registerDate(LocalDateTime.now())
//                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
//                .roles(Collections.singletonList(Role.ROLE_USER))
//                .accountNonExpired(true)
//                .accountNonLocked(true)
//                .credentialsNonExpired(true)
//                .enabled(true)   //TODO set "false" by default and enable account through administrator
//                .build();
//
//        if (userRepository.findAll().isEmpty())
//            user.setRoles(Collections.singletonList(Role.ROLE_ADMINISTRATOR));
//
//        userRepository.save(user);
//    }
//
//    public List<User> getUsers() {
//        return userRepository.findAll();
//    }
//
//    public List<UserDto> getUserDtoList() {
//        return getUsers().stream()
//                .map(u -> getUserDto(u.getUsername()))
//                .toList();
//    }
//
//    public UserDto getUserDto(String username) {
//        User user = getUserByUsername(username);
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        userDto.setRole(user.getRoles().get(0).name());
//        return userDto;
//    }
//
//    public User saveUserChanges(UserDto userDto) {
//        User user = getUserByUsername(userDto.getUsername());
//        user.setFirstname(userDto.getFirstname());
//        user.setLastname(userDto.getLastname());
//        user.setEmail(userDto.getEmail());
//        user.setShopServiceName(userDto.getShopServiceName());
//        user.setShopUrl(userDto.getShopUrl());
//
//        List<Role> roles = user.getRoles();
//        roles.clear();
//        roles.add(Role.valueOf(userDto.getRole()));
//        user.setRoles(roles);
//
//        return userRepository.save(user);
//    }
//
//    public void deleteUser(String username) {
//        User user = getUserByUsername(username);
//        userRepository.delete(user);
//    }


//     if(keycloak == null){
//
//        keycloak = KeycloakBuilder.builder()
//                .serverUrl(serverUrl)
//                .realm(realm)
//                .grantType(OAuth2Constants.PASSWORD)
//                .username(userName)
//                .password(password)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .resteasyClient(new ResteasyClientBuilder()
//                        .connectionPoolSize(10)
//                        .build();
//                                   )
//                    .build();
//    }


}
