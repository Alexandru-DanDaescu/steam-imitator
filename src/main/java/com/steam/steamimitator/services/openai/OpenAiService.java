package com.steam.steamimitator.services.openai;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAiService {

    private final RestTemplate restTemplate;
    private static final String APIKEY = "YOUR_KEY_HERE";
    private static final String OPEN_AI_URL = "https://api.openai.com/v1/chat/completions";
    private final RateLimiter rateLimiter = new RateLimiter(10.0);

    public OpenAiService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }



    public String getRecommendations(String prompt) {
        rateLimiter.acquire();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(APIKEY);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", List.of(message));
        request.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPEN_AI_URL, entity, String.class);

        return response.getBody();
    }
}
