
package CyLife.Websockets.chat;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @Column
    private int clubId; // New field added for clubId

    // Default constructor
    public Message() {}

    // New constructor with clubId
    public Message(String userName, String content, int clubId) {
        this.userName = userName;
        this.content = content;
        this.clubId = clubId;
    }

    // Getter and Setter for clubId
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    // Explicit getters for userName and content to resolve the issue
    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
