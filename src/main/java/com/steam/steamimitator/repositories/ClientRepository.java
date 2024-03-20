package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
