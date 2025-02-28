package com.example.learnhub.controllers;

import com.example.learnhub.entities.Canal;
import com.example.learnhub.services.CanalService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/canales")
@AllArgsConstructor
public class CanalController {

    private final CanalService canalService;


    @GetMapping("/search")
    public List<Canal> buscarCanal(@RequestParam String q) {
        try {
            return canalService.buscarPorCoincidencia(q);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error al buscar canales", e);
        }
    }
    @PostMapping("/newCanal")
    public String createCanal(@RequestBody Canal canal) throws ExecutionException, InterruptedException {
        return canalService.saveCanal(canal);
    }
    @PostMapping("/{channelId}/send")
    public String sendMessage(@PathVariable String channelId,
                              @RequestParam String message) {
        try {
            // Obtener el correo del contexto de seguridad
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = "";
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                System.out.println("Principal class: " + principal.getClass().getName());
                if (principal instanceof String) {
                    email = (String) principal;
                    String principalString = (String) principal;
                    principalString = principalString.split("@")[0];
                    //System.out.println("Contenido del Principal (String): " + principalString);}
                }
            }
            canalService.sendMessage(channelId, email, message);
            return "Mensaje enviado";
        } catch (Exception e) {
            return "Error al enviar el mensaje: " + e.getMessage();
        }
    }

    @GetMapping("/{channelId}/messages")
    public List<Object> getMessages(@PathVariable String channelId) {
        try {
            return canalService.getMessages(channelId).stream()
                    .map(doc -> (Object) doc.getData()) // Conversión explícita a Object
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los mensajes", e);
        }
    }
}