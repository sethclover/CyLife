package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

/**
 * Controller for managing groups of people.
 */
@RestController
public class Controller {

    // HashMap to store groups of people. Each group name maps to another HashMap of person IDs and persons.
    private HashMap<String, HashMap<Integer, Person>> groupMap = new HashMap<>();

    // GET: Retrieve all people from a specific group
    @GetMapping("/groups/{groupName}")
    public HashMap<Integer, Person> getPeopleInGroup(@PathVariable String groupName) {
        return groupMap.getOrDefault(groupName, new HashMap<>());
    }

    // POST: Add a new person to a group
    @PostMapping("/groups/{groupName}")
    public String addPersonToGroup(@PathVariable String groupName, @RequestBody Person person) {
        groupMap.putIfAbsent(groupName, new HashMap<>());
        HashMap<Integer, Person> peopleInGroup = groupMap.get(groupName);

        // Generate a unique ID (e.g., based on current size of the group)
        int newId = peopleInGroup.size() + 1;
        peopleInGroup.put(newId, person);

        return "Person added with ID: " + newId;
    }

    // PUT: Update a person in a group by their ID
    @PutMapping("/groups/{groupName}/{personId}")
    public String updatePersonInGroup(@PathVariable String groupName, @PathVariable int personId, @RequestBody Person updatedPerson) {
        HashMap<Integer, Person> peopleInGroup = groupMap.get(groupName);
        if (peopleInGroup != null && peopleInGroup.containsKey(personId)) {
            peopleInGroup.put(personId, updatedPerson);
            return "Person with ID " + personId + " updated.";
        }
        return "Person not found.";
    }

    // DELETE: Remove a person from a group by their ID
    @DeleteMapping("/groups/{groupName}/{personId}")
    public String deletePersonFromGroup(@PathVariable String groupName, @PathVariable int personId) {
        HashMap<Integer, Person> peopleInGroup = groupMap.get(groupName);
        if (peopleInGroup != null && peopleInGroup.remove(personId) != null) {
            return "Person with ID " + personId + " removed.";
        }
        return "Person not found.";
    }
}
