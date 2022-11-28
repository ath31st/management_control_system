package top.shop.gateway.util;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

public final class TokenExtractor {
    public static String getTokenAuthUser() {

        KeycloakAuthenticationToken keycloakAuthenticationToken =
                (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();
    }

    public static HttpHeaders headersWithTokenAuthUser() {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(getTokenAuthUser());
        return headers;
    }

    public static HttpEntity<Object> httpEntityWithTokenAuthUser(Object o){
       return new HttpEntity<>(o, TokenExtractor.headersWithTokenAuthUser());
    }

}
