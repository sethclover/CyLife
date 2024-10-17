package onetomany.Events;

import java.util.List;

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
}
