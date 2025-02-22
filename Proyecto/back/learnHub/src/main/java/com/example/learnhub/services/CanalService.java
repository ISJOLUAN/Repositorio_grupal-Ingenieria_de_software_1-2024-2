package com.example.learnhub.services;


import com.example.learnhub.entities.Canal;
import com.example.learnhub.repository.CanalRepository;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CanalService {
    @Autowired
    private CanalRepository canalRepository;

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
}