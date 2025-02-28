package com.example.learnhub.services;


import com.example.learnhub.entities.Canal;
import com.example.learnhub.repository.CanalRepository;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

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
        if (canal.getId() == null || canal.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del curso no puede ser nulo o vacío.");
        }

        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(canal.getId()).set(canal).get();
        return "canal guardado exitosamente: " + canal.getId();
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
}