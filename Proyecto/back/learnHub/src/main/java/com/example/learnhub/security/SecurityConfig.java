package com.example.learnhub.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.GET,"/login", "/logout", "/session").permitAll();
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oaut2 -> {
                    oaut2.successHandler((request, response, authentication) -> {
                        Map<String, Object> attributes;
                        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if(auth != null){
                            attributes = user.getAttributes();
                            String email = (String) attributes.get("email");
                            if(email.endsWith("@unal.edu.co")){
                                response.sendRedirect("https://learnhub-front.vercel.app/");
                            }else {
                                SecurityContextHolder.clearContext();
                                response.sendRedirect("//login?error=true");
                            }
                        }
                    });
                }).logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("https://docs.spring.io/spring-security/reference/servlet/architecture.html#requestcache-prevent-saved-request");
                    logout.clearAuthentication(true);
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                    SecurityContextHolder.clearContext();
                })
                .formLogin(Customizer.withDefaults())
                .build();

    }
}
