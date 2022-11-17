package top.shop.gateway.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        UserDto userDto = new UserDto();

        userDto.setUsername("admin");
        userDto.setFirstname("admin");
        userDto.setLastname("admin");
        userDto.setPassword("123");
        userDto.setRole("ROLE_ADMINISTRATOR");
        userDto.setEmail("admin@admin.com");

        userService.registerUser(userDto);
    }

}