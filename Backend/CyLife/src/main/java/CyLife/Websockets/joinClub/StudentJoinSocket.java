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
import org.springframework.transaction.annotation.Transactional;

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
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(StudentJoinSocket.class);

    @Transactional
    @OnOpen
    public void onOpen(Session session, @PathParam("clubId") String clubId, @PathParam("name") String name)
            throws IOException {
        logger.info("Entered into Open");

        // store connecting user information
        sessionUsernameMap.put(session, clubId);
        usernameSessionMap.put(clubId, session);

        if (name.equals(clubId)) {
            // Send chat history to the newly connected user
            sendMessageToParticularUser(clubId, getChatHistory());
            return;
        }

        String message = name + " has joined the club!";
        broadcast(message);

        // Saving chat history to repository
        msgRepo.save(new JoinClubMessage(clubId, message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

    private void sendMessageToParticularUser(String name, String message) {
        try {
            usernameSessionMap.get(name).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    // Gets the Chat history from the repository
    private String getChatHistory() {
        List<JoinClubMessage> messages = msgRepo.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if (messages != null && messages.size() != 0) {
            for (JoinClubMessage message : messages) {
                sb.append(message.getName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }
}