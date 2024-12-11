package CyLife;

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

public class JoinClubControllerTest {

    private JoinClubController joinClubController;
    private Session mockSession;
    private Basic mockRemote;

    @BeforeEach
    public void setUp() throws IOException {
        joinClubController = new JoinClubController();
        JoinClubMessageRepository mockRepository = mock(JoinClubMessageRepository.class);
        joinClubController.setMessageRepository(mockRepository);

        mockSession = mock(Session.class);
        mockRemote = mock(Basic.class);
        when(mockSession.getBasicRemote()).thenReturn(mockRemote);

        // Clear the session maps before each test
        JoinClubController.getSessionNameMap().clear();
        JoinClubController.getNameSessionMap().clear();
    }

    @Test
    public void testOnOpenAddsUserToSessionMaps() throws IOException {
        String clubId = "123";
        String name = "testUser";

        joinClubController.onOpen(mockSession, clubId, name);

        Map<Session, String> sessionNameMap = JoinClubController.getSessionNameMap();
        Map<String, Session> nameSessionMap = JoinClubController.getNameSessionMap();

        assertTrue(sessionNameMap.containsKey(mockSession));
        assertEquals(name, sessionNameMap.get(mockSession));

        assertTrue(nameSessionMap.containsKey(name));
        assertEquals(mockSession, nameSessionMap.get(name));
    }

    @Test
    public void testOnCloseRemovesUserFromSessionMaps() throws IOException {
        String clubId = "123";
        String name = "testUser";

        joinClubController.onOpen(mockSession, clubId, name);
        joinClubController.onClose(mockSession);

        assertFalse(JoinClubController.getSessionNameMap().containsKey(mockSession));
        assertFalse(JoinClubController.getNameSessionMap().containsKey(name));
    }

    @Test
    public void testOnOpenBroadcastsJoinMessage() throws IOException {
        String clubId = "123";
        String name = "testUser";

        doNothing().when(mockRemote).sendText(anyString());

        joinClubController.onOpen(mockSession, clubId, name);

        verify(mockRemote, times(1)).sendText(name + " has joined the club!");
    }
}
