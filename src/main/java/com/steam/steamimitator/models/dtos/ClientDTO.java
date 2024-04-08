package com.steam.steamimitator.models.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Validated
public class ClientDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Client fullName field cannot be empty")
    private String fullName;

    @NotNull(message = "Client dateOfBirth field is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Client gender field is required")
    private String gender;

    @NotEmpty(message = "Client phoneNumber field cannot be empty")
    private String phoneNumber;

    @NotNull(message = "Address is required")
    private AddressDTO address;
}
