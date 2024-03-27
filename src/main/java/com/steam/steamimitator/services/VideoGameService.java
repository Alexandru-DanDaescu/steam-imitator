package com.steam.steamimitator.services;

import com.steam.steamimitator.models.dtos.VideoGameDTO;

import java.util.List;

public interface VideoGameService {

    VideoGameDTO createVideoGame(VideoGameDTO videoGameDTO);
    List<VideoGameDTO> getVideoGames();
    VideoGameDTO updateVideoGame(Long id, VideoGameDTO videoGameDTO);
    void deleteVideoGame(Long id);
}
