package com.example.learnhub.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/data")
    public Map<String, Object> getSessionData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> sessionData = new HashMap<>();
        if(auth != null && auth.isAuthenticated()) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) auth.getPrincipal();
            sessionData = oAuth2User.getAttributes();
        }
        return sessionData;
    }
}
