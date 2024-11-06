
package CyLife.Clubs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Club {

    //ID collection and the names to call in Postman
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clubId;
    private String clubName;
    private String description;
    private String clubEmail;

    // Constructors, getters, and setters

    public Club() {
    }

    public Club(String clubName, String description, String clubEmail) {
        this.clubName = clubName;
        this.description = description;
        this.clubEmail = clubEmail;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
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
}
