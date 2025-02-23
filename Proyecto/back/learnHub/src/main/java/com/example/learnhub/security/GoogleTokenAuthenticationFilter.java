package com.example.learnhub.security;

import com.example.learnhub.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GoogleTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    private static final TokenService tokenService = new TokenService();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Ignorar el filtro para rutas públicas
        String path = request.getRequestURI();
        return path.startsWith("/learnhub") ||  // Excluye WebSocket y archivos estáticos
                Stream.of("/login", "/logout", "/session/user", "/session/dataUser","/default-ui.css","/index.html","/css/main.css","/js/main.js").anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {

            System.out.println("Solicitud recibida: " + request.getRequestURI());

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


