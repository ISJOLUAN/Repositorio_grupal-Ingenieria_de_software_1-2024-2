package com.example.learnhub.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/login", "/logout").permitAll();
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oaut2 -> {
                    oaut2.successHandler((request, response, authentication) -> {
                        try {
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
                                    response.sendRedirect("/login?error=true");
                                }
                            }
                        }catch (Exception e) {
                            log.error("e: ", e);
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    });
                }).logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login");
                    logout.clearAuthentication(true);
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                    SecurityContextHolder.clearContext();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new GoogleTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
