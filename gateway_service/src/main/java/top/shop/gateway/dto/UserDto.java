package top.shop.gateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.shop.gateway.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link User} entity
 */
@Data
@NoArgsConstructor
public class UserDto implements Serializable {

    @NotEmpty(message = "Firstname cannot be empty.")
    @Size(min = 3, max = 25, message = "Firstname must be minimum 3 and maximum 25 characters.")
    private String firstname;

    @NotEmpty(message = "Lastname cannot be empty.")
    @Size(min = 3, max = 25, message = "Lastname must be minimum 3 and maximum 25 characters.")
    private String lastname;

    @Email
    @Size(min = 1, max = 100)
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 3, max = 100, message = "Password must be minimum 3 and maximum 25 characters.")
    private String password;

    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 3, max = 25, message = "Username must be minimum 3 and maximum 25 characters.")
    private String username;

   // @NotEmpty(message = "Shop name cannot be empty.")
    private String shopName;

   // @NotEmpty(message = "Shop URL cannot be empty.")
    private String shopUrl;

    private LocalDateTime registerDate;
}