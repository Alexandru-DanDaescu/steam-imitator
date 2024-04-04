package com.steam.steamimitator.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "video_games")
public class VideoGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "developer")
    private String developer;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "price")
    private double price;

    @Column(name = "tags")
    private Set<String> tags;

    @Column(name = "genre")
    private String genre;

    @Column(name = "language_supported")
    private Set<String> languageSupported;

    @ManyToMany(mappedBy = "videoGames")
    private Set<Account> accounts = new HashSet<>();
}
