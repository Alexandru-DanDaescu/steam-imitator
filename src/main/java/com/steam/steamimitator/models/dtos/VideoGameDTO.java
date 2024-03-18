package com.steam.steamimitator.models.dtos;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

public class VideoGameDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Video game title field cannot be empty")
    private String title;

    @NotNull(message = "Video game price field cannot be empty")
    @Min(value = 1, message = "Video game price must be greater than 1")
    private double price;

    @NotEmpty(message = "Video game tags field cannot be empty")
    private Set<String> tags;

    @NotEmpty(message = "Video game genre field cannot be empty")
    private String genre;

    @NotEmpty(message = "Video game languageSupported cannot be empty")
    private Set<String> languageSupported;
}
