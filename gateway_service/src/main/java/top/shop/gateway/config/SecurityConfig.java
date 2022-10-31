package top.shop.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
//                .and()
//                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
//                .and()

//                .authorizeRequests().anyRequest().permitAll()
                .authorizeRequests()
                .antMatchers( "/css/**","/webjars/**", "/register").permitAll()
  //              .antMatchers("/index").hasAuthority(ROLE_USER.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/index");

        return httpSecurity.build();
    }
}
