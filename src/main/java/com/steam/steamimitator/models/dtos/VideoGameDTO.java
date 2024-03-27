package com.steam.steamimitator.models.dtos;




import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Set;
@Data
@Validated
public class VideoGameDTO implements Serializable {

    private Long id;

    @NotEmpty(message = "Video game title field cannot be empty")
    private String title;

    @NotEmpty(message = "Video game developer field cannot be empty")
    private String developer;

    @NotEmpty(message = "Video game publisher field cannot be empty")
    private String publisher;

    @NotNull(message = "Video game price field cannot be empty")
    @Min(value = 1, message = "Video game price must be greater than 1")
    private double price;

    @NotEmpty(message = "Video game tags list cannot be empty")
    private Set<String> tags;

    @NotEmpty(message = "Video game genre field cannot be empty")
    private String genre;

    @NotEmpty(message = "Video game languageSupported list cannot be empty")
    private Set<String> languageSupported;
}
