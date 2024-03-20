package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
