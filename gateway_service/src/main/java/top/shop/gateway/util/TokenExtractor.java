package top.shop.gateway.util;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import java.security.Principal;

public final class TokenExtractor {
    public static String getTokenAuthUser(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();
    }

}
