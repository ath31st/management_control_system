package top.shop.gateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import top.shop.gateway.util.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    private Long id;

    @Size(min = 3, max = 25)
    private String firstname;

    @Size(min = 3, max = 25)
    private String lastname;

    @Size(min = 3)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email
    @Size(min = 1, max = 100)
    private String email;

    @Size(min = 1, max = 25)
    private String username;

    private String shopServiceName;

    private String shopUrl;

    private LocalDateTime registerDate;

    @JsonIgnore
    private boolean accountNonExpired;

    @JsonIgnore
    private boolean accountNonLocked;

    @JsonIgnore
    private boolean credentialsNonExpired;

    @JsonIgnore
    private boolean enabled;

    private List<Role> roles;
}