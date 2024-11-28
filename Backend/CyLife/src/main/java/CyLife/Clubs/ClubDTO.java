package CyLife.Clubs;

public class ClubDTO {
    private int clubId;
    private String clubName;
    private String description;
    private String clubEmail;

    public ClubDTO(int clubId, String clubName, String description, String clubEmail) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description = description;
        this.clubEmail = clubEmail;
    }

    // Getters and Setters
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
