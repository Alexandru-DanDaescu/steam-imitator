package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.models.dtos.ClientDTO;
import com.steam.steamimitator.models.entities.Client;
import com.steam.steamimitator.repositories.AddressRepository;
import com.steam.steamimitator.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ObjectMapper objectMapper;

    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository,
                             ObjectMapper objectMapper) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.objectMapper = objectMapper;
    }

//    @Override
//    public ClientDTO createClient(ClientDTO clientDTO) {
//
//        Client client = objectMapper.convertValue(clientDTO, Client.class);
//
//        if() {
//
//        }
//    }

    @Override
    public List<ClientDTO> getClients() {
        return null;
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        return null;
    }

    @Override
    public void deleteClient(Long id) {

    }
}
