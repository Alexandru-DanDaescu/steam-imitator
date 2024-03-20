package com.steam.steamimitator.repositories;

import com.steam.steamimitator.models.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
