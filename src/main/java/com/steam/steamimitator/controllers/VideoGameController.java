package com.steam.steamimitator.controllers;

import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.services.VideoGameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @PostMapping("/video-games")
    public ResponseEntity<VideoGameDTO> createVideoGame(@RequestBody @Valid VideoGameDTO videoGameDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoGameService.createVideoGame(videoGameDTO));
    }

    @GetMapping("/video-games")
    public ResponseEntity<List<VideoGameDTO>> getVideoGames() {
        List<VideoGameDTO> videoGameDTOList = videoGameService.getVideoGames();
        if (videoGameDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(videoGameDTOList);
        }
    }

    @PutMapping("/video-games/{id}")
    public ResponseEntity<VideoGameDTO> updateVideoGame(@PathVariable Long id, @RequestBody VideoGameDTO videoGameDTO) {
        VideoGameDTO updateVideoGameDTO = videoGameService.updateVideoGame(id, videoGameDTO);
        return ResponseEntity.ok().body(updateVideoGameDTO);
    }

    @DeleteMapping("/video-games/{id}")
    public ResponseEntity<String> deleteVideoGame(@PathVariable Long id) {
        videoGameService.deleteVideoGame(id);
        return ResponseEntity.ok("Video game with id: " + id + " deleted successfully.");
    }
}
