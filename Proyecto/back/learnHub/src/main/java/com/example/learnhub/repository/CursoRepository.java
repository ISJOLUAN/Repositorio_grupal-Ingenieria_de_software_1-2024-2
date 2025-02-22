package com.example.learnhub.repository;

import com.example.learnhub.entities.Curso;
import com.example.learnhub.utils.StringUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class CursoRepository {
    private static final String COLLECTION_NAME = "cursos";


    public List<Curso> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(COLLECTION_NAME);

        // Obtener todos los documentos
        ApiFuture<QuerySnapshot> future = collectionReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Normalizar el término de búsqueda (minúsculas y sin tildes)
        String searchTermNormalized = StringUtils.removeTildes(searchTerm.toLowerCase());

        // Filtrar en memoria
        List<Curso> cursos = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Curso curso = document.toObject(Curso.class);

            // Normalizar los campos (minúsculas y sin tildes)
            String codigoNormalized = StringUtils.removeTildes(curso.getId() != null ? curso.getId().toLowerCase() : "");
            String nombreNormalized = StringUtils.removeTildes(curso.getNombre() != null ? curso.getNombre().toLowerCase() : "");
            String descripcionNormalized = StringUtils.removeTildes(curso.getDescripcion() != null ? curso.getDescripcion().toLowerCase() : "");

            // Verificar si el término de búsqueda coincide en alguno de los campos
            if (codigoNormalized.contains(searchTermNormalized) ||
                    nombreNormalized.contains(searchTermNormalized) ||
                    descripcionNormalized.contains(searchTermNormalized)) {
                cursos.add(curso);
            }
        }

        return cursos;
    }
}