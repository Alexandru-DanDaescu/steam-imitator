package com.steam.steamimitator.controllers;

import com.steam.steamimitator.exceptions.account.AccountNotFoundException;
import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.repositories.AccountRepository;
import com.steam.steamimitator.services.openai.VideoGameRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VideoGameRecommendationsController {

    private final VideoGameRecommendationService videoGameRecommendationService;
    private final AccountRepository accountRepository;

    public VideoGameRecommendationsController(VideoGameRecommendationService videoGameRecommendationService,
                                              AccountRepository accountRepository) {
        this.videoGameRecommendationService = videoGameRecommendationService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/video-games-reference/{accountId}")
    public ResponseEntity<String> getRecommendations(@PathVariable Long accountId) {
        Account account = accountRepository.getAccountById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id : " + accountId + " could not be found"));

        String recommendation = videoGameRecommendationService.generateRecommendations(account);
        return ResponseEntity.ok(recommendation);
    }
}
