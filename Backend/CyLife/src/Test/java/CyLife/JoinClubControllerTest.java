package CyLife;

import CyLife.Websockets.joinClub.JoinClubMessage;
import CyLife.Websockets.joinClub.JoinClubController;
import CyLife.Websockets.joinClub.JoinClubMessageRepository;
import jakarta.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.websocket.RemoteEndpoint.Basic;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class JoinClubControllerTest {

    private JoinClubController joinClubController;
    private JoinClubMessageRepository mockRepository;
    private Session mockSession1;
    private Session mockSession2;
    private Basic mockRemote1;
    private Basic mockRemote2;

    @BeforeEach
    public void setUp() throws IOException {
        joinClubController = new JoinClubController();
        mockRepository = mock(JoinClubMessageRepository.class);
        joinClubController.setMessageRepository(mockRepository);

        // Mock the first session and its remote endpoint
        mockSession1 = mock(Session.class);
        mockRemote1 = mock(Basic.class);
        when(mockSession1.getBasicRemote()).thenReturn(mockRemote1);

        // Mock the second session and its remote endpoint
        mockSession2 = mock(Session.class);
        mockRemote2 = mock(Basic.class);
        when(mockSession2.getBasicRemote()).thenReturn(mockRemote2);

        // Clear the session maps before each test
        JoinClubController.getSessionNameMap().clear();
        JoinClubController.getNameSessionMap().clear();
    }

    @Test
    public void testChatHistoryRetrieval() throws IOException {
        String clubId = "123";
        String name = clubId; // Special case where the name is the same as the clubId

        when(mockRepository.findByClubId(clubId)).thenReturn(
                List.of(new JoinClubMessage(clubId, name, "Hello World!"))
        );

        joinClubController.onOpen(mockSession1, clubId, name);

        verify(mockRemote1).sendText("Hello World!\n");
    }

    @Test
    public void testBroadcastToMultipleUsers() throws IOException {
        String clubId = "123";
        String user1 = "user1";
        String user2 = "user2";

        doNothing().when(mockRemote1).sendText(anyString());
        doNothing().when(mockRemote2).sendText(anyString());

        joinClubController.onOpen(mockSession1, clubId, user1);
        joinClubController.onOpen(mockSession2, clubId, user2);

        verify(mockRemote1, times(1)).sendText(user1 + " has joined the club!");
        verify(mockRemote2, times(1)).sendText(user2 + " has joined the club!");
    }

    @Test
    public void testOnError() {
        Throwable mockThrowable = mock(Throwable.class);
        when(mockThrowable.getMessage()).thenReturn("Test Exception");

        joinClubController.onError(mockSession1, mockThrowable);

        verify(mockThrowable, times(1)).printStackTrace();
    }

    @Test
    public void testReconnectingUser() throws IOException {
        String clubId = "123";
        String name = "testUser";

        joinClubController.onOpen(mockSession1, clubId, name);

        // Simulate user reconnecting with a new session
        joinClubController.onOpen(mockSession2, clubId, name);

        assertEquals(mockSession2, JoinClubController.getNameSessionMap().get(name));
    }

    @Test
    public void testEmptyChatHistory() throws IOException {
        String clubId = "123";
        String name = clubId; // Special case where the name is the same as the clubId

        when(mockRepository.findByClubId(clubId)).thenReturn(List.of());

        joinClubController.onOpen(mockSession1, clubId, name);

        verify(mockRemote1).sendText("");
    }

    @Test
    public void testBroadcastToNonExistentClub() {
        // No exception should be thrown if broadcasting to a non-existent club
        assertDoesNotThrow(() -> joinClubController.onOpen(mockSession1, "nonexistentClub", "user"));
    }
}
