package com.example.learnhub.service;

import com.example.learnhub.entity.Curso;
import com.example.learnhub.entity.Materia;
import com.example.learnhub.repositories.CursoRepository;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        return cursoRepository.buscarPorCoincidencia(searchTerm);
    }

    private static final String COLLECTION_NAME = "cursos";

    public String saveCurso(Curso curso) throws ExecutionException, InterruptedException {
        if (curso.getId() == null || curso.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del curso no puede ser nulo o vacío.");
        }

        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(curso.getId()).set(curso).get();
        return "Curso guardado exitosamente: " + curso.getId();
    }
}
