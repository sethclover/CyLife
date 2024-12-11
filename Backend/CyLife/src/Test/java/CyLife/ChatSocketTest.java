package CyLife;

import static CyLife.Websockets.chat.ChatSocket.sessionUserIdMap;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import CyLife.Users.User; // Import for the User class
import CyLife.Clubs.Club; // Import for the Club class
import CyLife.Websockets.chat.ChatSocket;
import CyLife.Websockets.chat.Message;
import CyLife.Websockets.chat.MessageRepository;
import CyLife.Users.UserRepository;
import CyLife.Clubs.ClubRepository;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Optional;

public class ChatSocketTest {

    @Mock
    private Session session;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ChatSocket chatSocket;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnOpen_ValidUserAndClub() throws IOException {
        // Mock user and club
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(new User("TestUser", "test@example.com", "password", User.UserType.STUDENT));
        when(clubRepository.findById(1)).thenReturn(Optional.of(new Club("Test Club", "Description", "club@example.com")));

        chatSocket.onOpen(session, "1", "1");

        // Verify behavior
        verify(session).getBasicRemote(); // Check if messages are sent
    }

    @Test
    public void testOnMessage_BroadcastMessage() throws IOException {
        // Mock user
        when(userRepository.findById(1)).thenReturn(new User("TestUser", "test@example.com", "password", User.UserType.STUDENT));
        when(session.getId()).thenReturn("sessionId");

        chatSocket.onMessage(session, "Hello, Club!", "1");

        // Verify message saved and broadcast
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    public void testOnError() {
        Throwable throwable = new RuntimeException("Simulated Error");

        chatSocket.onError(session, throwable);

        // No exceptions should occur; log should capture the error
    }

    @Test
    public void testOnOpen_InvalidUserId() throws IOException {
        when(userRepository.existsById(1)).thenReturn(false);

        chatSocket.onOpen(session, "1", "1");

        // Ensure session is not added to the map and is closed
        verify(session).close();
    }

}