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
import java.util.Map;

@Slf4j
@Service
public class OpenAiService {

    private final RestTemplate restTemplate;
    private final String apiKey = "YOUR_OPENAI_API_KEY";
    private final String openAIUrl = "https://api.openai.com/v1/engines/davinci-codex/completions";

    public OpenAiService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }



    public String getRecommendations(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("prompt", prompt);
        request.put("maxTokens", 200);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openAIUrl, entity, String.class);

        return response.getBody();
    }
}
