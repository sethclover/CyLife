// Source code is decompiled from a .class file using FernFlower decompiler.
package CyLife.Clubs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Club {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int clubId;
    private String clubName;
    private String description;
    private String clubEmail;

    public Club() {
    }

    public Club(String clubName, String description, String clubEmail) {
        this.clubName = clubName;
        this.description = description;
        this.clubEmail = clubEmail;
    }

    public int getClubId() {
        return this.clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return this.clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClubEmail() {
        return this.clubEmail;
    }

    public void setClubEmail(String clubEmail) {
        this.clubEmail = clubEmail;
    }
}