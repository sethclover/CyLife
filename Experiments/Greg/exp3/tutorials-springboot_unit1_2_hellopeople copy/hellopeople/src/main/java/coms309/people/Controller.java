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
    
    @GetMapping("/groups/{groupName}/{personId}")
    public Person getPersonById(@PathVariable String groupName, @PathVariable int personId) {
        HashMap<Integer, Person> peopleInGroup = groupMap.get(groupName);

        // Check if the group and person exist
        if (peopleInGroup != null && peopleInGroup.containsKey(personId)) {
            return peopleInGroup.get(personId);  // Return the person if found
        }

        // Return null or handle the case where the person or group does not exist
        return null;
    }


    @PostMapping("/groups/{groupName}")
    public String addPersonToGroup(@PathVariable String groupName, @RequestBody Person person) {
        // Initialize the group if it doesn't exist
        groupMap.putIfAbsent(groupName, new HashMap<>());

        // Retrieve the group
        HashMap<Integer, Person> peopleInGroup = groupMap.get(groupName);

        // Add the person to the group with the specified ID
        peopleInGroup.put(person.getId(), person);

        return "Person added with ID: " + person.getId();
    }


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
