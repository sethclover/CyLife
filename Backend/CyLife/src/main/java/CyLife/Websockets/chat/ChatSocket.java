
package CyLife.Websockets.chat;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import CyLife.Users.User;
import CyLife.Users.UserRepository;

@Controller
@ServerEndpoint(value = "/chat/{clubId}/{userId}")  // WebSocket URL with clubId and userId
public class ChatSocket {

	private static MessageRepository msgRepo;
	private static UserRepository userRepo;

	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;
	}

	@Autowired
	public void setUserRepository(UserRepository repo) {
		userRepo = repo;
	}

	private static Map<Session, String> sessionUserIdMap = new Hashtable<>();
	private static Map<String, Session> userIdSessionMap = new Hashtable<>();
	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("clubId") String clubId, @PathParam("userId") String userId) throws IOException {
		// Validate user existence
		if (!userRepo.existsById(Integer.parseInt(userId))) {
			session.close();
			logger.warn("Connection denied for invalid user ID: " + userId);
			return;
		}

		// Retrieve user's name
		String username = userRepo.findById(Integer.parseInt(userId)).getName();
		logger.info("User " + username + " (ID: " + userId + ") joined club chat " + clubId);

		// Store session and user details
		sessionUserIdMap.put(session, userId);
		userIdSessionMap.put(userId, session);

		// Send chat history and broadcast user join
		sendMessageToUser(userId, getChatHistory(Integer.parseInt(clubId)));
		String message = "User " + username + " joined the club chat";
		broadcastToClub(message, Integer.parseInt(clubId));
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("clubId") String clubId) throws IOException {
		logger.info("Message received: " + message);
		String userId = sessionUserIdMap.get(session);

		// Fetch user's name
		String username = userRepo.findById(Integer.parseInt(userId)).getName();

		broadcastToClub(username + ": " + message, Integer.parseInt(clubId));
		msgRepo.save(new Message(userId, message, Integer.parseInt(clubId)));
	}

	@OnClose
	public void onClose(Session session, @PathParam("clubId") String clubId) throws IOException {
		String userId = sessionUserIdMap.remove(session);
		String username = userRepo.findById(Integer.parseInt(userId)).getName();
		userIdSessionMap.remove(userId);
		broadcastToClub("User " + username + " left the chat", Integer.parseInt(clubId));
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.error("Error in WebSocket communication", throwable);
	}

	private void sendMessageToUser(String userId, String message) {
		try {
			userIdSessionMap.get(userId).getBasicRemote().sendText(message);
		} catch (IOException e) {
			logger.error("Error sending message to user", e);
		}
	}

	private void broadcastToClub(String message, int clubId) {
		sessionUserIdMap.forEach((session, uid) -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.error("Broadcast error", e);
			}
		});
	}

	private String getChatHistory(int clubId) {
		List<Message> messages = msgRepo.findByClubId(clubId);
		StringBuilder sb = new StringBuilder();
		for (Message msg : messages) {
			sb.append("User ").append(msg.getUserName()).append(": ").append(msg.getContent()).append("\n");
		}
		return sb.toString();
	}
}
