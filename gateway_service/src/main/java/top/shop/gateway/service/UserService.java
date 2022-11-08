package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRegisterDate(LocalDateTime.now());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList(Role.ROLE_MANAGER));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        userRepository.save(user);
    }

}
