package com.example.learnhub.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FirebaseInit {

    @PostConstruct
    public void initialize() {
        try {
            // Cargar el archivo JSON desde src/main/resources
            InputStream serviceAccount = new ClassPathResource("learnhub.json").getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
