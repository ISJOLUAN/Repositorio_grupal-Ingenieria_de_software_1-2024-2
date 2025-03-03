package com.example.learnhub.controllers;

import com.example.learnhub.entities.Canal;
import com.example.learnhub.entities.Estudiante;
import com.example.learnhub.services.CanalService;
import com.example.learnhub.services.EstudianteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/canales")
@AllArgsConstructor
public class CanalController {

    private final CanalService canalService;
    private final EstudianteService estudianteService;

    @GetMapping("/search")
    public List<Canal> buscarCanal(@RequestParam String q) {
        try {
            return canalService.buscarPorCoincidencia(q);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error al buscar canales", e);
        }
    }
    @PostMapping("/newCanal")
    public String createCanal(@RequestParam String nombre,
                              @RequestParam String descripcion,
                              @RequestParam String area,
                              @RequestParam(required = false, defaultValue = "100") int capacity) {
        try {
            // Validar campos requeridos
            if (nombre.trim().isEmpty() || descripcion.trim().isEmpty() || area.trim().isEmpty()) {
                return "Todos los campos son obligatorios.";
            }

            // Obtener el usuario autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "Error: Usuario no autenticado.";
            }

            String email = (String) auth.getPrincipal(); // Obtiene el email del usuario autenticado
            Estudiante admin = estudianteService.getEstudianteByEmail(email);
            // Crear el canal
            Canal canal = new Canal();
            canal.setNombre(nombre);
            canal.setDescripcion(descripcion);
            canal.setArea(area);
            canal.setCapacity(capacity);
            canal.setCurrentSize(1); // El creador ya es miembro
            canal.setAdministrador(admin); // Asigna al creador como administrador
            canal.setMiembros(List.of(admin)); // Agrega al creador en la lista de miembros

            // Guardar el canal en Firestore
            return canalService.saveCanal(canal);
        } catch (Exception e) {
            return "Error al crear el canal: " + e.getMessage();
        }
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

    @PostMapping("/unirse/{canalId}")
    public String unirseACanal(@PathVariable String canalId) throws ExecutionException, InterruptedException {
        return canalService.unirseACanal(canalId);
    }

    @GetMapping("/misCanales")
    public List<Canal> obtenerMisCanales() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (auth == null || !auth.isAuthenticated()) {
            return (List<Canal>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        try {
            String email = (String) auth.getPrincipal(); // Obtener el email del usuario
           // List<Canal> misCanales = canalService.obtenerMisCanales(email);
            //return ResponseEntity.ok(misCanales);
            return canalService.obtenerMisCanales(email);
        } catch (Exception e) {
            return (List<Canal>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los canales.");
        }
    }

    @GetMapping("/{canalId}/miembros")
    public ResponseEntity<?> obtenerMiembrosCanal(@PathVariable String canalId) {
        try {
            List<Map<String, String>> miembros = canalService.obtenerMiembrosCanal(canalId);
            return ResponseEntity.ok(miembros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los miembros.");
        }
    }

    @DeleteMapping("/deleteAllCanales")
    public String deleteAllCanales() throws ExecutionException, InterruptedException {
        return canalService.deleteAllCanales();
    }
}