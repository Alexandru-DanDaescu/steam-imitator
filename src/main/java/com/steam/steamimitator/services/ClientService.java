package com.steam.steamimitator.services;

import com.steam.steamimitator.models.dtos.ClientDTO;

import java.util.List;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO);
    List<ClientDTO> getClients();
    ClientDTO updateClient(Long id, ClientDTO clientDTO);
    void deleteClient(Long id);
}
