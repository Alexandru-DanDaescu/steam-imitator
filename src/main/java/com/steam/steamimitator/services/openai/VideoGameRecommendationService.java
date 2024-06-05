package com.steam.steamimitator.services.openai;

import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.models.entities.VideoGame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoGameRecommendationService {

    private final OpenAiService openAiService;

    public VideoGameRecommendationService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String generateRecommendations(Account account) {
        Map<String, Integer> tagFrequency = new HashMap<>();

        for(VideoGame game : account.getVideoGames()) {
            for(String tag : game.getTags()) {
                tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
            }
        }

        String mostCommonTags = tagFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));

        String prompt = "Recommend video games based on these tags: " + mostCommonTags;
        return openAiService.getRecommendations(prompt);
    }
}
