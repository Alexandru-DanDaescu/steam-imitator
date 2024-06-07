package com.steam.steamimitator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.exceptions.account.AccountCreateException;
import com.steam.steamimitator.exceptions.account.AccountNotFoundException;
import com.steam.steamimitator.exceptions.account.AccountUpdateException;
import com.steam.steamimitator.exceptions.client.ClientNotFoundException;
import com.steam.steamimitator.exceptions.videogame.VideoGameNotFoundException;
import com.steam.steamimitator.models.dtos.AccountDTO;
import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.models.entities.Client;
import com.steam.steamimitator.models.entities.VideoGame;
import com.steam.steamimitator.repositories.AccountRepository;
import com.steam.steamimitator.repositories.ClientRepository;
import com.steam.steamimitator.repositories.VideoGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private static final String ID_NOT_FOUND = " not found.";

    private final AccountRepository accountRepository;
    private final VideoGameRepository videoGameRepository;
    private final ClientRepository clientRepository;

    private final ObjectMapper objectMapper;

    public AccountServiceImpl(AccountRepository accountRepository, VideoGameRepository videoGameRepository,
                              ClientRepository clientRepository, ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.videoGameRepository = videoGameRepository;
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByEmail(accountDTO.getEmail()) ||
                accountRepository.existsByUserName(accountDTO.getUserName())) {
            throw new AccountCreateException("Account with this email or password already exists.");
        }

        Account account = objectMapper.convertValue(accountDTO, Account.class);

        Account savedAccountEntity = null;

        if (account.getUserName() != null || account.getPassword() != null || account.getEmail() != null) {
            savedAccountEntity = accountRepository.save(account);
        }
        return convertToDTO(savedAccountEntity);
    }

    @Override
    @Cacheable(value = "accounts")
    public List<AccountDTO> getAccounts() {

        try {
            List<Account> accountList = accountRepository.findAll();
            List<AccountDTO> accountDTOList = new ArrayList<>();

            for (Account account : accountList) {
                accountDTOList.add(convertToDTO(account));
            }

            if (accountDTOList.isEmpty()) {
                throw new AccountNotFoundException("Accounts can't be found because they don't exist.");
            }
            return accountDTOList;
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    @CachePut(value = "accounts", key = "#id")
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        try {
            Account updatedAccount = accountRepository.findById(id)
                    .map(account -> updateAccountValues(account, accountDTO))
                    .orElseThrow(() -> new AccountNotFoundException("Account with id: " + id + ID_NOT_FOUND));
            Account savedAccount = accountRepository.save(updatedAccount);
            return convertToDTO(savedAccount);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new AccountUpdateException("Failed to update account with id: " + id, e);
        }
    }

    @Override
    @CachePut(value = "accounts", key = "#clientId + '|' + #accountId")
    public void addClientToAccount(Long clientId, Long accountId) {
        try {
            Client client = clientRepository.getClientById(clientId)
                    .orElseThrow(() -> new ClientNotFoundException("Client with id: " + clientId + ID_NOT_FOUND));

            Account account = accountRepository.getAccountById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException("account with id:" + accountId + ID_NOT_FOUND));

            account.setClient(client);

            accountRepository.save(account);
        } catch (ClientNotFoundException | AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    @CachePut(value = "accounts", key = "#accountId + '|' + #videoGamesIds")
    public void addGamesToUserAccount(Long accountId, Long[] videoGamesIds) {
        try {
            Account account = accountRepository.getAccountById(accountId)
                    .orElseThrow(() -> new VideoGameNotFoundException("Account with id:" + accountId + ID_NOT_FOUND));

            for(Long videoGameId : videoGamesIds) {
                VideoGame videoGame = videoGameRepository.findById(videoGameId)
                        .orElseThrow(() -> new VideoGameNotFoundException("Videogame with id:" + videoGameId + ID_NOT_FOUND));

                if(!account.getVideoGames().contains(videoGame)) {
                    account.getVideoGames().add(videoGame);
                    videoGame.getAccounts().add(account);
                }
                else {
                    throw new VideoGameNotFoundException("Video game with id: " + videoGameId +
                            " is already associated with the account.");
                }
            }
            accountRepository.save(account);

        } catch (AccountNotFoundException | VideoGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    @CacheEvict(value = "accounts", key = "#id")
    public void deleteAccount(Long id) {
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException("Account with id: " + id +
                            " cannot be deleted because it couldn't be found"));
            accountRepository.delete(account);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private Account updateAccountValues(Account account, AccountDTO accountDTO) {
        account.setUserName(accountDTO.getUserName());
        account.setPassword(accountDTO.getPassword());
        account.setEmail(accountDTO.getEmail());
        account.setCreatedAt(accountDTO.getCreatedAt());

        return account;
    }

    private AccountDTO convertToDTO(Account account) {
        return objectMapper.convertValue(account, AccountDTO.class);
    }

}

