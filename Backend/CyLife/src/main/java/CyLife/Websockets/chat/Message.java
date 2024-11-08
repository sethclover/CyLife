
package CyLife.Websockets.chat;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String content;
    private int clubId;
    private Date sent = new Date();

    public Message() {}

    public Message(String userName, String content, int clubId) {
        this.userName = userName;
        this.content = content;
        this.clubId = clubId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }

    public Date getSent() { return sent; }
    public void setSent(Date sent) { this.sent = sent; }
}
