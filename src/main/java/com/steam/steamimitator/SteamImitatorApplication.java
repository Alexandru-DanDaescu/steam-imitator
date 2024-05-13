package com.steam.steamimitator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SteamImitatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamImitatorApplication.class, args);
	}

}
