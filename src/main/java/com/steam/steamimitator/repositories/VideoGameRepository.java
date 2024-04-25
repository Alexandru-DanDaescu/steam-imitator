package com.steam.steamimitator.repositories;
import com.steam.steamimitator.models.entities.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {

    boolean existsByTitle(String title);


    @Query("SELECT vg.id FROM Account a JOIN a.videoGames vg WHERE a.id = :accountId")
    List<Long> getVideoGameIds(@Param("accountId") Long accountId);
}
