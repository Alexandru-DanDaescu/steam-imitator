package com.steam.steamimitator.services;

import com.steam.steamimitator.models.dtos.AccountDTO;
import com.steam.steamimitator.models.dtos.VideoGameDTO;

import java.time.LocalDate;
import java.util.List;


public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    List<AccountDTO> getAccounts();

    List<VideoGameDTO> getVideoGamesBetweenDates(Long id, LocalDate startDate, LocalDate endDate);

    AccountDTO updateAccount(Long id, AccountDTO accountDTO);

    void deleteAccount(Long id);

    void addClientToAccount(Long clientId, Long accountId);

    void addGamesToUserAccount(Long accountId, Long[] videoGamesIds);
}
