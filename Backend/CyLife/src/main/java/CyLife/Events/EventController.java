package CyLife.Events;

import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    private String success = "{\"message\":\"Success\"}";
    private String failure = "{\"message\":\"Failure\"}";

    @GetMapping(path = "/events")
    List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping(path = "/upcomingEvents/{userId}")
    public List<Event> getUpcomingEvents(@PathVariable int userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysFromNow = currentDate.plusDays(7);

        return eventRepository.studentsEventsThisWeek(userId, currentDate, sevenDaysFromNow);
    }

    @GetMapping(path = "/events/{id}")
    Event getEventById(@PathVariable int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "/events")
    String createEvent(@RequestBody Event event) {
        if (event == null) {
            return failure;
        }
        else {
            eventRepository.save(event);
            return success;
        }
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event request) {
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent == null) {
            return null;
        }
        System.out.println();
        if (request.getClubId() != null) {
            existingEvent.setClubId(request.getClubId());
        }
        if (request.getEventName() != null) {
            existingEvent.setEventName(request.getEventName());
        }
        if (request.getDescription() != null) {
            existingEvent.setDescription(request.getDescription());
        }
        if (request.getEventLocation() != null) {
            existingEvent.setEventLocation(request.getEventLocation());
        }
        if (request.getDate() != null) {
            existingEvent.setDate(request.getDate());
        }

        eventRepository.save(existingEvent);
        return existingEvent;
    }

    @DeleteMapping(path = "/events/{id}")
    String deleteEvent(@PathVariable int id) {
        // Check if the event exists
        if (!eventRepository.existsById(id)) {
            return failure;
        }
        else {
            eventRepository.deleteById(id);
            return success;
        }
        
    }
}
