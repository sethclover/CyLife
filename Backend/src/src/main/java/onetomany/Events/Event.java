
package onetomany.Events;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;
    private String eventName;
    private String description;
<<<<<<< HEAD
    private String location_time;
=======
    private String eventLocation;
>>>>>>> 7aa477fd833dcede8e9860ffcbff5c5c6b568338

    // Constructors
    public Event() {
    }

    public Event(String eventName, String description, String location_time) {
        this.eventName = eventName;
        this.description = description;
        this.location_time = location_time;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public String getEventLocationTime() {
        return location_time;
    }

    public void setEventLocationTime(String location_time) {
        this.location_time = location_time;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
