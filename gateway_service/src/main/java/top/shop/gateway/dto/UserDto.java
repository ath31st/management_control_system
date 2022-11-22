package top.shop.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserDto implements Serializable {
    private String id;
    @NotEmpty(message = "Firstname cannot be empty.")
    @Size(min = 3, max = 25, message = "Firstname must be minimum 3 and maximum 25 characters.")
    private String firstname;
    @NotEmpty(message = "Lastname cannot be empty.")
    @Size(min = 3, max = 25, message = "Lastname must be minimum 3 and maximum 25 characters.")
    private String lastname;
    @Email
    @Size(min = 1, max = 100)
    private String email;
    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 3, max = 25, message = "Username must be minimum 3 and maximum 25 characters.")
    private String username;
    private String role;
   // @NotEmpty(message = "Shop name cannot be empty.")
    private String shopServiceName;
   // @NotEmpty(message = "Shop URL cannot be empty.")
    private String shopUrl;
    private LocalDateTime registerDate;
}