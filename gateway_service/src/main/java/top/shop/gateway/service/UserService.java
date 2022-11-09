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
        if (userRepository.getUser(userDto.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists!");

        User user = User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .shopName(Value.DEFAULT.name()) //TODO administrator must change this field
                .shopUrl(Value.DEFAULT.name()) //TODO administrator must change this field
                .registerDate(LocalDateTime.now())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList(Role.ROLE_MANAGER))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)   //TODO set "false" by default and enable account through administrator
                .build();

        userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getUserDtoList() {
        return getUsers().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .toList();
    }

}
