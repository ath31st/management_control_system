package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.entity.User;
import top.shop.gateway.repository.UserRepository;
import top.shop.gateway.util.Role;
import top.shop.gateway.util.Value;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUser(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.getUser(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void registerUser(UserDto userDto) {
        if (userRepository.getUser(userDto.getUsername()).isPresent() && userDto.getRole().equals(Role.ROLE_ADMINISTRATOR.name()))
            return;
        if (userRepository.getUser(userDto.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists!");

        User user = User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .shopServiceName(Value.DEFAULT.name()) //TODO administrator must change this field
                .shopUrl(Value.DEFAULT.name()) //TODO administrator must change this field
                .registerDate(LocalDateTime.now())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)   //TODO set "false" by default and enable account through administrator
                .build();

        if (userRepository.findAll().isEmpty())
            user.setRoles(Collections.singletonList(Role.ROLE_ADMINISTRATOR));

        userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getUserDtoList() {
        return getUsers().stream()
                .map(u -> getUserDto(u.getUsername()))
                .toList();
    }

    public UserDto getUserDto(String username) {
        User user = getUserByUsername(username);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setRole(user.getRoles().get(0).name());
        return userDto;
    }

    public User saveUserChanges(UserDto userDto) {
        User user = getUserByUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setShopServiceName(userDto.getShopServiceName());
        user.setShopUrl(userDto.getShopUrl());

        List<Role> roles = user.getRoles();
        roles.clear();
        roles.add(Role.valueOf(userDto.getRole()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public List<Role> getRoles() {
        return List.of(Role.values());
    }

}
