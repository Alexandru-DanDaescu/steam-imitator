package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam.steamimitator.models.dtos.AccountDTO;
import com.steam.steamimitator.models.dtos.VideoGameDTO;
import com.steam.steamimitator.models.entities.Account;
import com.steam.steamimitator.models.entities.Client;
import com.steam.steamimitator.models.entities.VideoGame;
import com.steam.steamimitator.repositories.AccountRepository;
import com.steam.steamimitator.repositories.ClientRepository;
import com.steam.steamimitator.repositories.VideoGameRepository;
import com.steam.steamimitator.services.AccountServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VideoGameRepository videoGameRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;


    @Test
    @DisplayName("Account created successfully")
    void testCreateAccount() {
        AccountDTO accountDTO = accountDTO();

        Account account = account();

        Account savedAccountEntity = new Account();
        savedAccountEntity.setId(account.getId());
        savedAccountEntity.setUserName(account.getUserName());
        savedAccountEntity.setPassword(account.getPassword());
        savedAccountEntity.setEmail(account.getEmail());
        savedAccountEntity.setCreatedAt(account.getCreatedAt());

        when(accountRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(accountRepository.existsByUserName(any(String.class))).thenReturn(false);
        when(objectMapper.convertValue(accountDTO, Account.class)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccountEntity);
        when(objectMapper.convertValue(savedAccountEntity, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO savedAccountDTO = accountServiceImpl.createAccount(accountDTO);

        assertEquals(accountDTO, savedAccountDTO);
    }

    @Test
    @DisplayName("Accounts retrieved successfully")
    void testGetAccounts() {
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();

        AccountDTO accountDTO1 = new AccountDTO();
        AccountDTO accountDTO2 = new AccountDTO();
        AccountDTO accountDTO3 = new AccountDTO();

        List<Account> allAccounts = Arrays.asList(account1, account2, account3);

        when(accountRepository.findAll()).thenReturn(allAccounts);

        when(objectMapper.convertValue(account1, AccountDTO.class)).thenReturn(accountDTO1);
        when(objectMapper.convertValue(account2, AccountDTO.class)).thenReturn(accountDTO2);
        when(objectMapper.convertValue(account3, AccountDTO.class)).thenReturn(accountDTO3);


        List<AccountDTO> accountDTOSInDB = accountServiceImpl.getAccounts();

        assertEquals(3, accountDTOSInDB.size());
        assertTrue(accountDTOSInDB.contains(accountDTO1));
        assertTrue(accountDTOSInDB.contains(accountDTO2));
        assertTrue(accountDTOSInDB.contains(accountDTO3));

        verify(accountRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("VideoGames between dates found successfully")
    void testGetVideoGamesBetweenDates() {
        Account account = account();

        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 1);

        VideoGame videoGame1 = new VideoGame(1L,
                "Game 1",
                LocalDate.of(2020, 6, 15),
                "Dev 1", "Pub 1",
                59.99, Set.of("Action"),
                "Genre 1", Set.of("EN"), new HashSet<>());

        VideoGame videoGame2 = new VideoGame(2L,
                "Game 2",
                LocalDate.of(2020, 6, 15),
                "Dev 2", "Pub 2",
                49.99, Set.of("Adventure"),
                "Genre 2", Set.of("EN"), new HashSet<>());

        account.setVideoGames(Set.of(videoGame1, videoGame2));

        when(accountRepository.getAccountById(account.getId())).thenReturn(Optional.of(account));

        List<VideoGameDTO> result = accountServiceImpl.getVideoGamesBetweenDates(account.getId(), startDate, endDate);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Client added to account successfully")
    void testAddClientToAccount() {
        Account account = account();
        Client client = client();

        when(clientRepository.getClientById(client.getId())).thenReturn(Optional.of(client));
        when(accountRepository.getAccountById(account.getId())).thenReturn(Optional.of(account));

        accountServiceImpl.addClientToAccount(client.getId(), account.getId());

        verify(clientRepository).getClientById(client.getId());
        verify(accountRepository).getAccountById(account.getId());
        verify(accountRepository).save(account);

        assertEquals(client, account.getClient());
    }

    @Test
    @DisplayName("Account deleted successfully")
    void testDeleteAccount() {
        Long accountId = 1L;

        Account account = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountServiceImpl.deleteAccount(accountId);

        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Games added to account successfully")
    void testAddGamesToAccount() {
        Account account = account();
        Long[] videoGamesIds = {10L, 20L, 30L};

        when(accountRepository.getAccountById(account.getId())).thenReturn(Optional.of(account));

        List<VideoGame> videoGameList = new ArrayList<>();


        for (Long gameId : videoGamesIds) {
            VideoGame videoGame = new VideoGame();
            videoGame.setId(gameId);
            videoGameList.add(videoGame);
            when(videoGameRepository.findById(gameId)).thenReturn(Optional.of(videoGame));
        }

        assertDoesNotThrow(() -> accountServiceImpl.addGamesToUserAccount(account.getId(), videoGamesIds));

        assertEquals(videoGameList.size(), account.getVideoGames().size());
        assertTrue(account.getVideoGames().containsAll(videoGameList));

        verify(accountRepository, times(1)).getAccountById(account.getId());
        for (Long gameId : videoGamesIds) {
            verify(videoGameRepository, times(1)).findById(gameId);
        }
        verify(accountRepository, times(1)).save(account);
    }

    private static AccountDTO accountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setUserName("user");
        accountDTO.setPassword("password");
        accountDTO.setEmail("email@example.com");
        accountDTO.setCreatedAt(LocalDateTime.now());

        return accountDTO;
    }

    private static Account account() {
        Account account = new Account();
        account.setId(1L);
        account.setUserName("user");
        account.setPassword("password");
        account.setEmail("email@example.com");
        account.setCreatedAt(LocalDateTime.now());

        return account;
    }

    private static Client client() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("test name");
        client.setDateOfBirth(LocalDate.parse("2005-01-01"));
        client.setGender("male");
        client.setPhoneNumber("01243345");
        client.setAccount(account());

        return client;
    }
}
