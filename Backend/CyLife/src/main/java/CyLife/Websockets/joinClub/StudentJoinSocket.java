package CyLife.Websockets.joinClub;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/joinClub/{clubId}/{name}") // this is Websocket url

public class StudentJoinSocket {

    // cannot autowire static directly (instead we do it by the below
    // method
    private static JoinClubMessageRepository msgRepo;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context. This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
    @Autowired
    public void setMessageRepository(JoinClubMessageRepository repo) {
        msgRepo = repo; // we are setting the static variable
    }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionNameMap = new Hashtable<>();
    private static Map<String, Session> nameSessionMap = new Hashtable<>();

    private static Map<String, Map<Session, String>> clubSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(StudentJoinSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("clubId") String clubId, @PathParam("name") String name)
            throws IOException {
        logger.info("Entered into Open for club: " + clubId);

        // Ensure the map for this clubId exists
        clubSessionMap.computeIfAbsent(clubId, k -> new Hashtable<>());

        // Store the session for this clubId
        clubSessionMap.get(clubId).put(session, name);

        // store connecting user information
        sessionNameMap.put(session, name);
        nameSessionMap.put(name, session);

        if (name.equals(clubId)) {
            logger.info("Getting chat history");
            String chatHistory = getChatHistory(clubId);
            session.getBasicRemote().sendText(chatHistory);
            return;
        }

        String message = name + " has joined the club!";
        broadcastToClub(clubId, message);

        // Saving chat history to repository
        JoinClubMessage msg = new JoinClubMessage(clubId, name, message);
        msgRepo.save(msg);
        logger.info("Message saved to repository: " + message);

        session.close();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionNameMap.get(session);
        sessionNameMap.remove(session);
        nameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private void broadcastToClub(String clubId, String message) {
        Map<Session, String> sessionNameMap = clubSessionMap.get(clubId);

        if (sessionNameMap != null) {
            sessionNameMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Exception: " + e.getMessage(), e);
                }
            });
        }
    }

    // Gets the Chat history from the repository
    private String getChatHistory(String clubId) {
        List<JoinClubMessage> messages = msgRepo.findByClubId(clubId);

        if (messages.isEmpty()) {
            logger.info("No chat history found.");
            return "";
        }

        // Convert the list to a single string for printing
        StringBuilder sb = new StringBuilder();
        for (JoinClubMessage message : messages) {
            sb.append(message.getContent()).append("\n");
        }

        String history = sb.toString();
        logger.info("Chat History:\n" + history); // Log the chat history
        return history;
    }

}