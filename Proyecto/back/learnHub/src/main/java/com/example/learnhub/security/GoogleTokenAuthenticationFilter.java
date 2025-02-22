package com.example.learnhub.security;


import com.example.learnhub.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GoogleTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    private static final TokenService tokenService = new TokenService();
    // Lista de rutas públicas que deben ignorar la validación del token
    private static final String[] PUBLIC_PATHS = {"/login", "/logout","/session/user"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {

            // Verificar si la solicitud es para una ruta pública
            String path = request.getRequestURI();
            if (Arrays.asList(PUBLIC_PATHS).contains(path)) {
                filterChain.doFilter(request, response); // Ignorar validación para rutas públicas
                return;
            }

            // Extraer el token del encabezado Authorization
            String header = request.getHeader(HEADER_STRING);
            System.out.println("header: " + header);

            if (header == null || !header.startsWith(TOKEN_PREFIX)) {
                throw new RuntimeException("Token no proporcionado o inválido");
            }

            String token = header.replace(TOKEN_PREFIX, "");

            // Validar el token con Google
            if (!tokenService.validateToken(token)) {
                throw new RuntimeException("Token inválido");
            }

            // Extraer información del usuario (opcional)
            String email = TokenService.getEmailFromToken(token);
            System.out.println(email);

            // Configurar la autenticación en el contexto de seguridad
            CustomAuthentication auth = new CustomAuthentication(email, token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Manejar errores de autenticación
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Acceso no autorizado: " + e);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}


