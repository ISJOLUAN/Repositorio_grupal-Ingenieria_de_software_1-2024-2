package com.example.learnhub.repositories;

import com.example.learnhub.entity.Materia;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import com.example.learnhub.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MateriaRepository {

    private static final String COLLECTION_NAME = "materias";

    public Materia findByCodigo(String codigo) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Recupera el documento usando el campo 'codigo'
        ApiFuture<DocumentSnapshot> future = dbFirestore.collection(COLLECTION_NAME).document(codigo).get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Materia.class);
        } else {
            return null;
        }
    }

    public List<Materia> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(COLLECTION_NAME);

        // Obtener todos los documentos
        ApiFuture<QuerySnapshot> future = collectionReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Normalizar el término de búsqueda (minúsculas y sin tildes)
        String searchTermNormalized = StringUtils.removeTildes(searchTerm.toLowerCase());

        // Filtrar en memoria
        List<Materia> materias = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Materia materia = document.toObject(Materia.class);

            // Normalizar los campos (minúsculas y sin tildes)
            String codigoNormalized = StringUtils.removeTildes(materia.getCodigo() != null ? materia.getCodigo().toLowerCase() : "");
            String nombreNormalized = StringUtils.removeTildes(materia.getNombre() != null ? materia.getNombre().toLowerCase() : "");
            String descripcionNormalized = StringUtils.removeTildes(materia.getDescripcion() != null ? materia.getDescripcion().toLowerCase() : "");

            // Verificar si el término de búsqueda coincide en alguno de los campos
            if (codigoNormalized.contains(searchTermNormalized) ||
                    nombreNormalized.contains(searchTermNormalized) ||
                    descripcionNormalized.contains(searchTermNormalized)) {
                materias.add(materia);
            }
        }

        return materias;
    }
}