package com.example.learnhub.services;

import com.example.learnhub.entities.Estudiante;
import com.example.learnhub.repository.EstudianteRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class EstudianteService {

    private static final String STUDENT = "estudiantes";

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public void saveEstudiante() throws ExecutionException, InterruptedException {
        Estudiante estudiante = estudianteRepository.toEstudent();
        if (estudiante != null) {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collectionReference = firestore.collection(STUDENT);
            ApiFuture<QuerySnapshot> future = collectionReference.get();

            for (QueryDocumentSnapshot document : future.get().getDocuments()) {
                if (Objects.requireNonNull(document.getString("correo")).equals(estudiante.getCorreo())) return;
            }

            firestore.collection(STUDENT).document(estudiante.getHash()).set(estudiante).get();
        }
    }

}
