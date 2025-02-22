package com.example.learnhub.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;


@Service
public class TokenService {

    private static final String TOKEN_INFO_URL="https://www.googleapis.com/oauth2/v3/tokeninfo";

    private final String clientIdApp = System.getenv("GOOGLE_CLIENT_ID");

    public boolean validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = TOKEN_INFO_URL+"?access_token="+token;
        System.out.println("Mi client ID" + clientIdApp);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println(response);
            if(response != null) {
                String aud = (String) response.get("aud");
                return aud.equals(clientIdApp);
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public static String getEmailFromToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        try {
            Map<String, Object> userInfo = restTemplate.getForObject(url, Map.class);
            return (String) userInfo.get("email");
        } catch (Exception e) {
            return null; // Error al obtener la información del usuario
        }
    }

}
