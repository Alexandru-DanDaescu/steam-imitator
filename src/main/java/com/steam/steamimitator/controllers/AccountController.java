package com.steam.steamimitator.controllers;

import com.steam.steamimitator.models.dtos.AccountDTO;
import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDTO));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        List<AccountDTO> accountDTOList = accountService.getAccounts();
        if (accountDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(accountDTOList);
        }
    }

    @GetMapping("account-video-games/{accountId}/{startDate}/{endDate}")
    public ResponseEntity<List<VideoGameDTO>> getVideoGamesBetweenDates(@PathVariable Long accountId,
                                                                        @PathVariable
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        LocalDate startDate,
                                                                        @PathVariable
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        LocalDate endDate) {
        List<VideoGameDTO> videoGameDTOList = accountService.getVideoGamesBetweenDates(accountId, startDate, endDate);
        return ResponseEntity.ok(videoGameDTOList);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        AccountDTO updateAccountDTO = accountService.updateAccount(id, accountDTO);
        return ResponseEntity.ok().body(updateAccountDTO);
    }

    @PatchMapping("/accounts/{accountId}/clients/{clientId}")
    public ResponseEntity<String> addClientToAccount(@PathVariable Long clientId, @PathVariable Long accountId) {
        accountService.addClientToAccount(clientId, accountId);
        return ResponseEntity.ok("Client with id: " + clientId +
                " successfully added to account with id: " + accountId);
    }

    @PatchMapping("/account-video-games/{accountId}/{videoGamesIds}")
    public ResponseEntity<String> addGamesToUserAccount(@PathVariable Long accountId,
                                                        @PathVariable Long[] videoGamesIds) {
        accountService.addGamesToUserAccount(accountId, videoGamesIds);
        return ResponseEntity.ok("Video games with ids: " +
                Arrays.toString(videoGamesIds) + " added successfully to account with id: " + accountId);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account with id: " + id + " successfully deleted");
    }
}
