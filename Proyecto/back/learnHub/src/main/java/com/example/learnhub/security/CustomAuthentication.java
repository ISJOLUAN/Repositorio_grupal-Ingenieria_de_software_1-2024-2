package com.example.learnhub.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthentication extends AbstractAuthenticationToken {

    private final String email;
    private final String token;

    public CustomAuthentication(String email, String token) {
        super(null); // Sin roles/autoridades en este ejemplo
        this.email = email;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token; // El token actúa como credenciales
    }

    @Override
    public Object getPrincipal() {
        return email; // El email actúa como principal
    }
}