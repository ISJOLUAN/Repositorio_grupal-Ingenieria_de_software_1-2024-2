package com.example.learnhub.repository;

import com.example.learnhub.entities.Estudiante;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EstudianteRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    public Estudiante toEstudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> data = new HashMap<>();

        if(auth != null && auth.isAuthenticated()) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) auth.getPrincipal();
            data.put("hash", oAuth2User.getAttributes().get("at_hash"));
            data.put("correo",oAuth2User.getAttributes().get("email"));
            data.put("give_name",oAuth2User.getAttributes().get("given_name"));
            data.put("family_name",oAuth2User.getAttributes().get("family_name"));
            data.put("picture",oAuth2User.getAttributes().get("picture"));
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Estudiante estudiante = mapper.convertValue(data, Estudiante.class);
        return estudiante;
    }

}
