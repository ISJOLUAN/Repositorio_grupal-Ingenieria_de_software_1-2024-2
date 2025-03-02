package com.example.learnhub.services;


import com.example.learnhub.entities.Canal;
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


    public CanalService(CanalRepository canalRepository) {
        this.canalRepository = canalRepository;
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

        // Verificar si el usuario ya está en la lista de miembros
        if (canal.getMiembros().contains(email)) {
            return "Ya eres miembro de este canal.";
        }

        // Verificar que el canal no esté lleno
        if (canal.getCurrentSize() >= canal.getCapacity()) {
            return "El canal ha alcanzado su capacidad máxima.";
        }

        // Agregar el usuario a la lista de miembros
        canal.getMiembros().add(email);
        canal.setCurrentSize(canal.getCurrentSize() + 1);

        // Actualizar Firestore con la nueva lista de miembros
        canalRef.set(canal);

        return "Te has unido al canal correctamente.";
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