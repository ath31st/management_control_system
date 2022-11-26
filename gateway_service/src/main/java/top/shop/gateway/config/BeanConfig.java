package top.shop.gateway.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    private String userName = "admin";
    private String password = "123";

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userName)
                .password(password)
                .clientId(clientId)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    @RequestScope
//    public RestTemplate restTemplate(HttpServletRequest httpServletRequest) {
//        // retrieve the auth header from incoming request
//        final String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//        final RestTemplate restTemplate = new RestTemplate();
//        // add a token if an incoming auth header exists, only
//        if (authHeader != null && !authHeader.isEmpty()) {
//            // since the header should be added to each outgoing request,
//            // add an interceptor that handles this.
//            restTemplate.getInterceptors().add(
//                    (outReq, bytes, clientHttpReqExec) -> {
//                        outReq.getHeaders().set(
//                                HttpHeaders.AUTHORIZATION, authHeader
//                        );
//                        return clientHttpReqExec.execute(outReq, bytes);
//                    });
//        }
//        return restTemplate;
//    }

}
