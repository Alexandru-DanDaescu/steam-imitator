package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.exceptions.client.ClientCreateException;
import com.steam.steamimitator.exceptions.client.ClientNotFoundException;
import com.steam.steamimitator.exceptions.client.ClientUpdateException;
import com.steam.steamimitator.models.dtos.ClientDTO;
import com.steam.steamimitator.models.entities.Address;
import com.steam.steamimitator.models.entities.Client;
import com.steam.steamimitator.repositories.AddressRepository;
import com.steam.steamimitator.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

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
        if (clientRepository.existsByFullName(clientDTO.getFullName())) {
            throw new ClientCreateException("Client already exists");
        }

        Client clientEntity = objectMapper.convertValue(clientDTO, Client.class);

        if (clientEntity.getAddress() != null) {
            Address addressEntity = addressRepository.save(clientEntity.getAddress());
            clientEntity.setAddress(addressEntity);
        }

        Client savedClientEntity = clientRepository.save(clientEntity);
        return convertToDTO(savedClientEntity);
    }

    @Override
    @Cacheable(value = "clients")
    public List<ClientDTO> getClients() {
        try {
            List<Client> clientList = clientRepository.findAll();
            List<ClientDTO> clientDTOList = new ArrayList<>();

            for (Client client : clientList) {
                clientDTOList.add(convertToDTO(client));
            }

            if (clientDTOList.isEmpty()) {
                throw new ClientNotFoundException("Clients couldn't be found because they don't exist");
            }
            return clientDTOList;
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    @Cacheable(value = "clientsCriteria", key = "#fullName + '|' + #dateOfBirth.toString() + '|' + #gender")
    public List<ClientDTO> sortClientsByCriteria(String fullName, LocalDate dateOfBirth, String gender) {

        try {
            List<Client> clientList = clientRepository.findAll();

            List<ClientDTO> clientDTOList = clientList.stream()
                    .filter(client -> client.getFullName().equals(fullName))
                    .filter(client -> client.getDateOfBirth().equals(dateOfBirth))
                    .filter(client -> client.getGender().equals(gender))
                    .map(this::convertToDTO)
                    .toList();

            if (clientDTOList.isEmpty()) {
                throw new ClientNotFoundException("Clients with matching criteria couldn't be found");
            }

            return clientDTOList;
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    @CacheEvict(value = "clients", key = "#id")
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        try {
            Client updatedClient = clientRepository.findById(id)
                    .map(client -> updateClientValues(client, clientDTO))
                    .orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + "not found."));

            Client savedClient = clientRepository.save(updatedClient);
            return convertToDTO(savedClient);
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ClientUpdateException("Failed to update client with id: " + id, e);
        }
    }

    @Override
    @CacheEvict(value = "clients", key = "#id")
    public void deleteClient(Long id) {
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " couldn't be found."));
            clientRepository.delete(client);
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    private Client updateClientValues(Client client, ClientDTO clientDTO) {
        client.setFullName(clientDTO.getFullName());
        client.setDateOfBirth(clientDTO.getDateOfBirth());
        client.setGender(clientDTO.getGender());
        client.setPhoneNumber(clientDTO.getPhoneNumber());

        return client;
    }

    private ClientDTO convertToDTO(Client client) {
        return objectMapper.convertValue(client, ClientDTO.class);
    }
}
