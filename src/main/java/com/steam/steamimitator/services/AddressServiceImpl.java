package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.repositories.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    private final ObjectMapper objectMapper;

    public AddressServiceImpl(AddressRepository addressRepository, ObjectMapper objectMapper){
        this.addressRepository = addressRepository;
        this.objectMapper = objectMapper;
    }
}
