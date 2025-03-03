package com.example.learnhub.services;


import com.example.learnhub.entities.Canal;
import com.example.learnhub.entities.Estudiante;
import com.example.learnhub.repository.CanalRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class CanalService {

    private final CanalRepository canalRepository;
    private final EstudianteService estudianteService;

    public CanalService(CanalRepository canalRepository, EstudianteService estudianteService) {
        this.canalRepository = canalRepository;
        this.estudianteService = estudianteService;
    }

    public List<Canal> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        return canalRepository.buscarPorCoincidencia(searchTerm);
    }

    private static final String COLLECTION_NAME = "canales";

    public String saveCanal(Canal canal) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Generar un ID único para el canal
        DocumentReference docRef = dbFirestore.collection(COLLECTION_NAME).document();
        canal.setId(docRef.getId());

        // Si la lista de miembros es null, inicializarla como lista vacía
        if (canal.getMiembros() == null) {
            canal.setMiembros(new ArrayList<>());
        }

        // Guardar el canal en Firestore
        ApiFuture<WriteResult> future = docRef.set(canal);
        future.get();

        return "Canal creado exitosamente con ID: " + canal.getId();
    }

    public void sendMessage(String channelId, String senderEmail, String message) throws ExecutionException, InterruptedException {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderEmail", senderEmail);
        messageData.put("message", message);
        messageData.put("timestamp", System.currentTimeMillis());

        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("canales")
                .document(channelId)
                .collection("chat")
                .add(messageData)
                .get();
    }
    public List<QueryDocumentSnapshot> getMessages(String channelId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection("canales")
                .document(channelId)
                .collection("chat")
                .orderBy("timestamp")
                .get()
                .get()
                .getDocuments();
    }

    public String unirseACanal(String canalId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference canalRef = dbFirestore.collection(COLLECTION_NAME).document(canalId);
        DocumentSnapshot canalSnapshot = canalRef.get().get();

        if (!canalSnapshot.exists()) {
            return "El canal no existe.";
        }

        Canal canal = canalSnapshot.toObject(Canal.class);
        if (canal == null) {
            return "Error al obtener los datos del canal.";
        }

        // Obtener el email del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "Usuario no autenticado.";
        }

        String email = (String) auth.getPrincipal();
        Estudiante miembro = estudianteService.getEstudianteByEmail(email);
        // Verificar si el usuario ya está en la lista de miembros
        if (canal.getMiembros().contains(email)) {
            return "Ya eres miembro de este canal.";
        }

        // Verificar que el canal no esté lleno
        if (canal.getCurrentSize() >= canal.getCapacity()) {
            return "El canal ha alcanzado su capacidad máxima.";
        }

        // Agregar el usuario a la lista de miembros
        canal.getMiembros().add(miembro);
        canal.setCurrentSize(canal.getCurrentSize() + 1);

        // Actualizar Firestore con la nueva lista de miembros
        canalRef.set(canal);

        return "Te has unido al canal correctamente.";
    }

    public List<Canal> obtenerMisCanales(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference canalesRef = dbFirestore.collection("canales");

        // Obtener todos los canales
        ApiFuture<QuerySnapshot> future = canalesRef.get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();
        Estudiante miembro = estudianteService.getEstudianteByEmail(email);
        List<Canal> misCanales = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documentos) {
            Canal canal = doc.toObject(Canal.class);

            // Verificar si el usuario es administrador o miembro del canal
            if (canal.getAdministrador().equals(miembro) || canal.getMiembros().contains(miembro)) {
                misCanales.add(canal);
            }
        }
        return misCanales;
    }

    public List<Map<String, String>> obtenerMiembrosCanal(String canalId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference canalRef = dbFirestore.collection("canales").document(canalId);
        DocumentSnapshot canalDoc = canalRef.get().get();

        if (!canalDoc.exists()) {
            throw new IllegalArgumentException("El canal no existe.");
        }

        Canal canal = canalDoc.toObject(Canal.class);
        if (canal == null) {
            throw new IllegalArgumentException("Error al obtener los datos del canal.");
        }

        List<Map<String, String>> miembrosLista = new ArrayList<>();

        // Obtener datos del administrador
        // Obtener datos del administrador
        Estudiante administrador = canal.getAdministrador();
        if (administrador != null) {
            Map<String, String> adminData = new HashMap<>();
            adminData.put("nombre", administrador.getNombreCompleto()); // Usa el nuevo método
            adminData.put("email", administrador.getCorreo());
            adminData.put("rol", "Administrador");
            miembrosLista.add(adminData);
        }

// Obtener datos de los miembros
        for (Estudiante miembro : canal.getMiembros()) {
            if (miembro != null) {
                Map<String, String> miembroData = new HashMap<>();
                miembroData.put("nombre", miembro.getNombreCompleto()); // Usa el nuevo método
                miembroData.put("email", miembro.getCorreo());
                miembroData.put("rol", "Miembro");
                miembrosLista.add(miembroData);
            }
        }

        return miembrosLista;
    }

    public String deleteAllCanales() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference canalesRef = dbFirestore.collection(COLLECTION_NAME);

        ApiFuture<QuerySnapshot> future = canalesRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete();
        }

        return "Todos los canales han sido eliminados.";
    }
}