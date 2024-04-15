package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByFullName(String fullName);

    @Query("""
            SELECT c FROM Client c WHERE c.id = :id
            """)
    Optional<Client> getClientById(Long id);
}
