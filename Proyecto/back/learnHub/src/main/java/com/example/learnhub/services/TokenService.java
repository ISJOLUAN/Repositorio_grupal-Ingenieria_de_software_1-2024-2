package com.example.learnhub.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;


@Service
public class TokenService {

    private static final String TOKEN_INFO_URL="https://www.googleapis.com/oauth2/v3/tokeninfo";

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientIdApp;

    public Boolean validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = TOKEN_INFO_URL+"?access_token="+token;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println(response);
            if(response != null) {
                String aud = (String)response.get("aud");
                return aud.equals(clientIdApp);
            }
            return Boolean.FALSE;
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

}
