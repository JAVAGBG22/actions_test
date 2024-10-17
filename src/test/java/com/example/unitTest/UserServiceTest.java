package com.example.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    // Mock the UserRepository to simulate database operations
    // mocke user repository för att simulera databas operationer
    // @Mock: skapar själva mock instancen av userRespository.
    @Mock
    private UserRepository userRepository;

    // injecera mocksen till userService
    //@InjectMocks: skapar en instance av userService och injecerar mockade repositoryt
    @InjectMocks
    private UserService userService;

    // Initialize alla mocks ovan
    // @BeforeEach: körs för varje test metod för att initiera mocksen.
    @BeforeEach
    void setUp() {
        // below, initierar de annoterade mocksen och injecerar dom där de behövs
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange:
        // skapa en sample user utan id
        // förbereda ett savedUser object med assignar id för att imitera
        // beteendet i save metoden
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        // Mock the behavior of userRepository.save to return the user with an ID
        // mocka beteendet av userRepository.save för att returnera usern med ett id
        User savedUser = new User();
        savedUser.setId("12345");
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setEmail(user.getEmail());

        // den här raden talar om för Mockito att returnera savedUser när save kallas
        // på med en user
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act: Call the createUser method
        User result = userService.createUser(user);

        // Assert: verifiera att usern blev sparad och att den returnerade
        // usern har ett id
        // se till att det är ett non-null id
        // verifiera att alla fält matchar
        assertNotNull(result.getId(), "Saved user should have an ID");
        assertEquals("John", result.getFirstName(), "First name should match");
        assertEquals("Doe", result.getLastName(), "Last name should match");
        assertEquals("john.doe@example.com", result.getEmail(), "Email should match");

        // vi använder verify() för att kolla så att userRepo.save blev invokad exakt en gång
        // med korrekt argument.
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testGetUserById_UserExists() {
        // Arrange: definiera ett user id och skapa en korresponderande user
        String userId = "12345";
        User user = new User();
        user.setId(userId);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");

        // Mock userRepository.findById(userId) för att returnera en Optional som innehåller usern
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act: kalla på getUserById med det specifika user id
        User result = userService.getUserById(userId);

        // Assert: se till att den returnerade user inte är null
        // och att alla fält marchar förväntade värden
        // Verify that findById was invoked exactly once with the correct userId.
        assertNotNull(result, "Returned user should not be null");
        assertEquals(userId, result.getId(), "User ID should match");
        assertEquals("Jane", result.getFirstName(), "First name should match");
        assertEquals("Smith", result.getLastName(), "Last name should match");
        assertEquals("jane.smith@example.com", result.getEmail(), "Email should match");

        // Verify that userRepository.findById was called once with the correct ID
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {
        // Arrange: Define a user ID that does not exist
        // Definiera ett användar-ID som inte motsvarar någon användare i arkivet.
        String userId = "nonexistent_id";

        //Mock userRepository.findById(userId) för att returnera en tom Optional.
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert: Call getUserById and expect a RuntimeException
        // Använd assertThrows för att verifiera att anrop av getUserById med det
        // obefintliga ID:t ger en RuntimeException.
        //Kontrollera att undantagsmeddelandet innehåller den förväntade texten.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        }, "Expected getUserById to throw, but it didn't");

        // Verify the exception message
        assertTrue(exception.getMessage().contains("User not found with id: " + userId));

        // Verify that userRepository.findById was called once with the correct ID
        // Se till att findById anropades exakt en gång med rätt användar-ID.
        verify(userRepository, times(1)).findById(userId);
    }

    /*
    * Ytterligare tips och bästa praxis
Testnamnkonventioner:
Använd beskrivande namn som indikerar scenariot som testas, t.ex.
* testCreateUser_Success, testGetUserById_UserExists, etc.
*
Arrange-Act-Assert (AAA)-mönster:
Arrange: Ställ in testdata och skenbeteenden.
Act: Utför metoden som testas.
Assert: Verifiera resultaten.
*
Mock Only externa beroenden:
Eftersom vi testar service layer, hånar vi arkivet för att isolera tjänstens logik.
*
Undvik att testa ramar:
Fokusera på att testa affärslogiken snarare än själva ramverket. Testa till
* exempel inte om Springs @Autowired fungerar.

    * */


}

