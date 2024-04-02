package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.exceptions.videogame.VideoGameNotFoundException;
import com.steam.steamimitator.exceptions.videogame.VideoGameUpdateException;
import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.models.entities.VideoGame;
import com.steam.steamimitator.repositories.VideoGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VideoGameServiceImpl implements VideoGameService {

    private final VideoGameRepository videoGameRepository;
    private final ObjectMapper objectMapper;

    public VideoGameServiceImpl(VideoGameRepository videoGameRepository, ObjectMapper objectMapper) {
        this.videoGameRepository = videoGameRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public VideoGameDTO createVideoGame(VideoGameDTO videoGameDTO) {
        VideoGame videoGameEntity = objectMapper.convertValue(videoGameDTO, VideoGame.class);

        VideoGame videoGameSavedEntity = null;

        if (videoGameEntity.getTitle() != null || videoGameEntity.getDeveloper() != null ||
                videoGameEntity.getPublisher() != null) {
            videoGameSavedEntity = videoGameRepository.save(videoGameEntity);
        }

        return convertToDTO(videoGameSavedEntity);
    }

    @Override
    public List<VideoGameDTO> getVideoGames() {
        try {
            List<VideoGame> videoGameList = videoGameRepository.findAll();
            List<VideoGameDTO> videoGameDTOList = new ArrayList<>();

            for (VideoGame videoGame : videoGameList) {
                videoGameDTOList.add(convertToDTO(videoGame));
            }

            if (videoGameDTOList.isEmpty()) {
                throw new VideoGameNotFoundException("Video games can't be found because they don't exist");
            }
            return videoGameDTOList;
        } catch (VideoGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    public VideoGameDTO updateVideoGame(Long id, VideoGameDTO videoGameDTO) {
        try {
            VideoGame updatedVideoGame = videoGameRepository.findById(id)
                    .map(videoGame -> updateVideoGameValues(videoGame, videoGameDTO))
                    .orElseThrow(() -> new VideoGameNotFoundException("Video game with id: " + id + " not found"));
            VideoGame savedVideoGame = videoGameRepository.save(updatedVideoGame);
            return convertToDTO(savedVideoGame);
        } catch (VideoGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new VideoGameUpdateException("Failed to update video game with id: " + id, e);
        }
    }

    @Override
    public void deleteVideoGame(Long id) {
        try {
            VideoGame videoGame = videoGameRepository.findById(id)
                    .orElseThrow(() -> new VideoGameNotFoundException("Video game with id: " + id +
                            " couldn't be deleted because it couldn't be found"));
            videoGameRepository.delete(videoGame);
        } catch (VideoGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private VideoGame updateVideoGameValues(VideoGame videoGame, VideoGameDTO videoGameDTO) {
        videoGame.setTitle(videoGameDTO.getTitle());
        videoGame.setDeveloper(videoGameDTO.getDeveloper());
        videoGame.setPublisher(videoGameDTO.getPublisher());
        videoGame.setPrice(videoGameDTO.getPrice());
        videoGame.setTags(videoGameDTO.getTags());
        videoGame.setGenre(videoGameDTO.getGenre());

        return videoGame;
    }

    private VideoGameDTO convertToDTO(VideoGame videoGame) {
        return objectMapper.convertValue(videoGame, VideoGameDTO.class);
    }
}
