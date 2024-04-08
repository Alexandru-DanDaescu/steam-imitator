package com.steam.steamimitator.controllers;


import com.steam.steamimitator.models.dtos.ClientDTO;
import com.steam.steamimitator.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ResponseEntity<ClientDTO> createClient(@RequestBody @Valid ClientDTO clientDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientDTO));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clientDTOList = clientService.getClients();
        if (clientDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clientDTOList);
        }
    }

    @GetMapping("/clients-criteria")
    public ResponseEntity<List<ClientDTO>> sortClientsByCriteria(@RequestParam(required = false) String fullName,
                                                                 @RequestParam(required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 LocalDate dateOfBirth,
                                                                 @RequestParam(required = false) String gender) {

        List<ClientDTO> clientDTOList = clientService.sortClientsByCriteria(fullName,dateOfBirth,gender);

        if (clientDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clientDTOList);
        }
    }

    @PutMapping("clients/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        ClientDTO updateCLientDTO = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok().body(updateCLientDTO);
    }

    @DeleteMapping("clients/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client with id: " + id + " is deleted successfully.");
    }
}
