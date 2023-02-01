package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/before-we-start")
    public String before() {
        return "before-we-start";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
//        keycloakSessionLogout(request);
//        tomcatSessionLogout(request);
        request.logout();
        return "redirect:/index";
    }

    private void keycloakSessionLogout(HttpServletRequest request){
        RefreshableKeycloakSecurityContext c = getKeycloakSecurityContext(request);
        KeycloakDeployment d = c.getDeployment();
        c.logout(d);
    }

    private void tomcatSessionLogout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

    private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request){
        return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}
