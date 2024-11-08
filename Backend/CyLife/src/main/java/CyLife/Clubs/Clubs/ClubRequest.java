package CyLife.Clubs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ClubRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    private int studentId;
    private String clubName;
    private String description;
    private String clubEmail;
    private String status; // Status could be "PENDING", "APPROVED", or "DECLINED"

    // Constructors, getters, and setters

    public ClubRequest() {
    }

    public ClubRequest(int studentId, String clubName, String description, String clubEmail, String status) {
        this.studentId = studentId;
        this.clubName = clubName;
        this.description = description;
        this.clubEmail = clubEmail;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClubEmail() {
        return clubEmail;
    }

    public void setClubEmail(String clubEmail) {
        this.clubEmail = clubEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
