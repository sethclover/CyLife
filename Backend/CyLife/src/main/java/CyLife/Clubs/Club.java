package CyLife.Clubs;

import CyLife.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clubId;

    private String clubName;
    private String description;
    private String clubEmail;

    @ManyToMany(mappedBy = "clubs")
    @JsonIgnore
    private Set<User> users = new HashSet<>();


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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
