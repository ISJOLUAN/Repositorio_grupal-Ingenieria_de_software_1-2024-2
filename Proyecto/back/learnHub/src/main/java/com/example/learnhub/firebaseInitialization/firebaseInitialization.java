package com.example.learnhub.firebaseInitialization;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class firebaseInitialization {

    @PostConstruct
    public void initialize(){
        FileInputStream serviceAccount = null;
        try {
            String firebaseCredentialsPath = System.getenv("FIREBASE_CREDENTIALS_PATH");

            if (firebaseCredentialsPath == null || firebaseCredentialsPath.isEmpty()) {
                throw new IllegalArgumentException("La variable de entorno FIREBASE_CREDENTIALS_PATH no está configurada.");
            }

            // Usar la ruta proporcionada para cargar el archivo JSON
            serviceAccount = new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            /*
            FirebaseApp.initializeApp(options);
            serviceAccount = new FileInputStream("./learnhub.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();*/

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


