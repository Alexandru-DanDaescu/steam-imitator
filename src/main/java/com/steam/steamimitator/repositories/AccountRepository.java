package com.steam.steamimitator.repositories;


import com.steam.steamimitator.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("""
            SELECT a FROM Account a WHERE a.id = :id
            """)
    Optional<Account> getAccountById(Long id);

}
