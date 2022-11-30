package top.shop.gateway.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.gateway.service.UserService;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserService userService;
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void run(String... args) {
        createAdminKeycloakUser();
    }

    private void createAdminKeycloakUser() {
        if (!keycloak.realm(realm).users().searchByUsername("admin", true).isEmpty()) return;

        UserRepresentation ur = new UserRepresentation();
        ur.setEnabled(true);
        ur.setUsername("admin");
        ur.setFirstName("admin");
        ur.setLastName("admin");
        ur.setEmail("admin@admin.com");
        ur.setAttributes(Map.of(
                "shopServiceName", List.of("DEFAULT"),
                "shopUrl", List.of("DEFAULT")));

        Response response = keycloak.realm(realm).users().create(ur);
        String userId = CreatedResponseUtil.getCreatedId(response);

        UserResource userResource = keycloak
                .realm(realm)
                .users()
                .get(userId);

        List<RoleRepresentation> list = userResource
                .roles()
                .realmLevel()
                .listAll();

        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setTemporary(false);
        cr.setType("password");
        cr.setValue("123");

        userResource.resetPassword(cr);
        userResource.roles().realmLevel().remove(list);
        userResource.roles().realmLevel().add(List.of(userService.getRoleRepresentation("ADMINISTRATOR")));
    }

}