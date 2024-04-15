package com.steam.steamimitator.services;

import com.steam.steamimitator.models.dtos.ClientDTO;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO);

    List<ClientDTO> getClients();

    List<ClientDTO> sortClientsByCriteria(String fullName, LocalDate dateOfBirth, String gender);

    ClientDTO updateClient(Long id, ClientDTO clientDTO);

    void deleteClient(Long id);
}
