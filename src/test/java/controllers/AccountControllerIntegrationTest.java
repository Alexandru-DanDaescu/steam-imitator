package controllers;

import com.steam.steamimitator.SteamImitatorApplication;
import com.steam.steamimitator.models.dtos.AccountDTO;
import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.services.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SteamImitatorApplication.class)
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("When creating account, return status code 201")
    void whenCreateAccountTest() throws Exception {
        String accountJson = """
                {
                  "userName" : "user",
                  "password" : "password",
                  "email" : "email@example.com",
                  "createdAt" : "2021-01-01T12:20:20"
                }
                """;

        AccountDTO mockAccountDTO = createAccountDTO();

        given(accountService.createAccount(any(AccountDTO.class))).willReturn(mockAccountDTO);
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value(mockAccountDTO.getUserName()))
                .andExpect(jsonPath("$.password").value(mockAccountDTO.getPassword()))
                .andExpect(jsonPath("$.email").value(mockAccountDTO.getEmail()))
                .andExpect(jsonPath("$.createdAt").value("2021-01-01T12:20:20"))
                .andReturn();
    }

    @Test
    @DisplayName("When accounts are present, return status code 200 and the account list")
    void whenAccountsArePresentTest() throws Exception {
        AccountDTO accountDTO1 = createAccountDTO();
        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setId(2L);
        accountDTO2.setUserName("something");
        accountDTO2.setPassword("mist");
        accountDTO2.setEmail("test@gmail.com");
        accountDTO2.setCreatedAt(LocalDateTime.parse("2015-03-14T08:10:50"));

        List<AccountDTO> accountDTOList = List.of(accountDTO1, accountDTO2);

        given(accountService.getAccounts()).willReturn(accountDTOList);

        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(accountDTO1.getId()))
                .andExpect(jsonPath("$[0].userName").value(accountDTO1.getUserName()))
                .andExpect(jsonPath("$[0].password").value(accountDTO1.getPassword()))
                .andExpect(jsonPath("$[0].email").value(accountDTO1.getEmail()))
                .andExpect(jsonPath("$[0].createdAt").value("2021-01-01T12:20:20"))
                .andExpect(jsonPath("$[1].id").value(accountDTO2.getId()))
                .andExpect(jsonPath("$[1].userName").value(accountDTO2.getUserName()))
                .andExpect(jsonPath("$[1].password").value(accountDTO2.getPassword()))
                .andExpect(jsonPath("$[1].email").value(accountDTO2.getEmail()))
                .andExpect(jsonPath("$[1].createdAt").value("2015-03-14T08:10:50"));
    }

    @Test
    @DisplayName("When adding games to user account, return status code 200 and success message")
    void whenAddGamesToUserAccountTest() throws Exception {
        Long accountId = 1L;
        Long[] videoGamesIds = {1L, 2L, 3L};

        String message = "Video games with ids: [1, 2, 3] added successfully to account with id: 1";

        mockMvc.perform(patch("/api/account-video-games/{accountId}/{videoGamesIds}", accountId, "1,2,3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(message));

        verify(accountService).addGamesToUserAccount(accountId, videoGamesIds);
    }

    private static AccountDTO createAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account().getId());
        accountDTO.setUserName(account().getUserName());
        accountDTO.setPassword(account().getPassword());
        accountDTO.setEmail(account().getEmail());
        accountDTO.setCreatedAt(account().getCreatedAt());

        return accountDTO;
    }

    private static Account account() {
        Account account = new Account();
        account.setId(1L);
        account.setUserName("user");
        account.setPassword("password");
        account.setEmail("email@example.com");
        account.setCreatedAt(LocalDateTime.parse("2021-01-01T12:20:20"));

        return account;
    }

}
