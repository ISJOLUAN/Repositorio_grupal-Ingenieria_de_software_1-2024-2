package com.example.learnhub.service;

import com.example.learnhub.entity.Materia;
import com.example.learnhub.repositories.MateriaRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class MateriaService {
    @Autowired
    private MateriaRepository materiaRepository;

    public List<Materia> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        return materiaRepository.buscarPorCoincidencia(searchTerm);
    }

    public Materia getMateriaByCodigo(String codigo) throws ExecutionException, InterruptedException {
        return materiaRepository.findByCodigo(codigo);
    }


    private static final String COLLECTION_NAME = "materias";

    public String saveMateria(Materia materia) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Usa el campo 'codigo' como ID del documento
        dbFirestore.collection(COLLECTION_NAME).document(materia.getCodigo()).set(materia).get();

        return "Materia guardada exitosamente: " + materia.getCodigo();
    }

    public String saveMaterias(List<Materia> materias) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        for (Materia materia : materias) {
            dbFirestore.collection(COLLECTION_NAME)
                    .document(materia.getCodigo())
                    .set(materia)
                    .get();
        }

        return "Todos los productos han sido guardados exitosamente.";
    }


}
