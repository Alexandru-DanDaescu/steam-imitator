package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.exceptions.client.ClientNotFoundException;
import com.steam.steamimitator.exceptions.client.ClientUpdateException;
import com.steam.steamimitator.models.dtos.ClientDTO;
import com.steam.steamimitator.models.entities.Address;
import com.steam.steamimitator.models.entities.Client;
import com.steam.steamimitator.repositories.AddressRepository;
import com.steam.steamimitator.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {

        Client clientEntity = objectMapper.convertValue(clientDTO, Client.class);

        if(clientEntity.getAddress() != null){
            Address addressEntity = addressRepository.save(clientEntity.getAddress());
            clientEntity.setAddress(addressEntity);
        }

        Client savedClientEntity = clientRepository.save(clientEntity);
        return convertToDTO(savedClientEntity);
    }

    @Override
    public List<ClientDTO> getClients() {
        try {
            List<Client> clientList = clientRepository.findAll();
            List<ClientDTO> clientDTOList = new ArrayList<>();

            for(Client client : clientList){
                clientDTOList.add(convertToDTO(client));
            }

            if(clientDTOList.isEmpty()) {
                throw new ClientNotFoundException("Clients couldn't be found because they dont exist");
            }
            return clientDTOList;
        } catch (ClientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        try {
            Client updatedClient = clientRepository.findById(id)
                    .map(client -> updateClientValues(client, clientDTO))
                    .orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + "not found."));

            Client savedClient = clientRepository.save(updatedClient);
            return convertToDTO(savedClient);
        } catch (ClientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        catch (Exception e){
            throw new ClientUpdateException("Failed to update client with id: " + id, e);
        }
    }

    @Override
    public void deleteClient(Long id) {
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " couldn't be found."));
            clientRepository.delete(client);
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    private Client updateClientValues(Client client, ClientDTO clientDTO){
        client.setFullName(clientDTO.getFullName());
        client.setDateOfBirth(clientDTO.getDateOfBirth());
        client.setGender(clientDTO.getGender());
        client.setPhoneNumber(clientDTO.getPhoneNumber());

        return client;
    }

    private ClientDTO convertToDTO(Client client){
        return objectMapper.convertValue(client, ClientDTO.class);
    }
}
