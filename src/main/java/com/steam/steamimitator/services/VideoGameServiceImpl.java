package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.repositories.VideoGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VideoGameServiceImpl implements VideoGameService{

    private final VideoGameRepository videoGameRepository;
    private final ObjectMapper objectMapper;

    public VideoGameServiceImpl(VideoGameRepository videoGameRepository, ObjectMapper objectMapper) {
        this.videoGameRepository = videoGameRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public VideoGameDTO createVideoGame(VideoGameDTO videoGameDTO) {
        return null;
    }

    @Override
    public List<VideoGameDTO> getVideoGames() {
        return null;
    }

    @Override
    public VideoGameDTO updateVideoGame(Long id, VideoGameDTO videoGameDTO) {
        return null;
    }

    @Override
    public void deleteVideoGame(Long id) {

    }
}
