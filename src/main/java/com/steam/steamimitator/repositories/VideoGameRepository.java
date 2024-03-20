package com.steam.steamimitator.repositories;
import com.steam.steamimitator.models.entities.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
}
