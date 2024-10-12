package onetomany.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import onetomany.Clubs.Club;
import onetomany.Organisation.Organisation;

@Entity
public class User {

    // Unique ID for the user, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    // User's name, email, and password fields
    private String name;
    private String email;
    private String password;

    // Enum to define the type of user (STUDENT, STAFF, CLUB, ORG)
    @Enumerated(EnumType.STRING)
    private UserType type;

    // Many-to-one relationship with Organisation entity
    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organisation organisation;

    // Many-to-one relationship with Club entity
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public enum UserType {
        STUDENT, STAFF, CLUB, ORG
    }

    // Constructors, getters, and setters

    public User() {
    }

    public User(String name, String email, String password, UserType type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}