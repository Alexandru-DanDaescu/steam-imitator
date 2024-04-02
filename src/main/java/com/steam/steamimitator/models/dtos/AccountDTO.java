package com.steam.steamimitator.models.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Validated
public class AccountDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Account username cannot be empty")
    private String userName;

    @NotEmpty(message = "Account password cannot be empty")
    private String password;

    @NotEmpty(message = "Account email cannot be empty")
    private String email;

    @NotNull(message = "Account createdAt field is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
