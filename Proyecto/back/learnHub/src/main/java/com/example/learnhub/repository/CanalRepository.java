package com.example.learnhub.repository;

import com.example.learnhub.entities.Canal;
import com.example.learnhub.utils.StringUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class CanalRepository {

    private static final String COLLECTION_NAME = "canales";

    public List<Canal> buscarPorCoincidencia(String searchTerm) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(COLLECTION_NAME);

        // Obtener todos los documentos
        ApiFuture<QuerySnapshot> future = collectionReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Normalizar el término de búsqueda (minúsculas y sin tildes)
        String searchTermNormalized = StringUtils.removeTildes(searchTerm.toLowerCase());

        // Filtrar en memoria
        List<Canal> canales = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Canal canal = document.toObject(Canal.class);

            // Normalizar los campos (minúsculas y sin tildes)
            String codigoNormalized = StringUtils.removeTildes(canal.getId() != null ? canal.getId().toLowerCase() : "");
            String nombreNormalized = StringUtils.removeTildes(canal.getNombre() != null ? canal.getNombre().toLowerCase() : "");
            String descripcionNormalized = StringUtils.removeTildes(canal.getDescripcion() != null ? canal.getDescripcion().toLowerCase() : "");
            String areaNormalized = StringUtils.removeTildes(canal.getArea() != null ? canal.getArea().toLowerCase() : "");

            // Verificar si el término de búsqueda coincide en alguno de los campos
            if (codigoNormalized.contains(searchTermNormalized) ||
                    nombreNormalized.contains(searchTermNormalized) ||
                    areaNormalized.contains(searchTermNormalized)||
                    descripcionNormalized.contains(searchTermNormalized)) {
                canales.add(canal);
            }
        }

        return canales;
    }
}