package top.shop.gateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.shop.gateway.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
@NoArgsConstructor
public class UserDto implements Serializable {
    @Size(min = 1, max = 25)
    private String firstname;
    @Size(min = 1, max = 25)
    private String lastname;
    @Size(min = 1, max = 100)
    private String email;
    @Size(min = 6)
    private String password;
    @Email
    private String username;
}