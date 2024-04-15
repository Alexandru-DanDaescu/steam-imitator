package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
