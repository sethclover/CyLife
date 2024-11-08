package CyLife.Websockets.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import CyLife.Clubs.ClubRepository;
import CyLife.Clubs.Club;
import CyLife.Websockets.chat.Message;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ClubRepository clubRepository;
    // Endpoint to retrieve all active chats with club information
    @GetMapping("/active")
    public List<Club> getActiveChats() {
        // Optionally, you could filter only clubs with recent messages
        return clubRepository.findAll(); // Or customize to filter based on active status
    }
    // Endpoint to retrieve chat history for a specific club
    @GetMapping("/history/{clubId}")
    public List<Message> getChatHistory(@PathVariable int clubId) {
        return messageRepository.findByClubId(clubId);
    }
}
