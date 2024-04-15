package com.steam.steamimitator.services;

import com.steam.steamimitator.models.dtos.AccountDTO;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    List<AccountDTO> getAccounts();

    AccountDTO updateAccount(Long id, AccountDTO accountDTO);

    AccountDTO addGamesToAccount(Long accountId, Long[] gameIds);

    void deleteAccount(Long id);

    void addClientToAccount(Long clientId, Long accountId);
}
