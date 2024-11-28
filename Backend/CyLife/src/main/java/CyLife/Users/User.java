package CyLife.Users;

import CyLife.Clubs.Club;
import CyLife.Events.Event;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType type = UserType.STUDENT; // Default type is STUDENT

    @ManyToMany
    @JoinTable(
            name = "user_events", // Join table for users and events
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key to User
            inverseJoinColumns = @JoinColumn(name = "event_id") // Foreign key to Event
    )
    private Set<Event> events = new HashSet<>();

    // Getters and Setters
    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @ManyToMany
    @JoinTable(
            name = "user_clubs", // The name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key to User
            inverseJoinColumns = @JoinColumn(name = "club_id") // Foreign key to Club
    )
    private Set<Club> clubs = new HashSet<>();

    public enum UserType {
        STUDENT, STAFF
    }

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

    public Set<Club> getClubs() {
        return clubs;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }
}