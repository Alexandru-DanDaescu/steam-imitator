package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.models.entities.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("""
            SELECT a FROM Account a WHERE a.id = :id
            """)
    Optional<Account> getAccountById(Long id);

}
