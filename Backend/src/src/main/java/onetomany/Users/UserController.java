package onetomany.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import onetomany.Clubs.Club;
import onetomany.Clubs.ClubRepository;
import onetomany.Organisation.Organisation;
import onetomany.Organisation.OrganisationRepository;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    ClubRepository clubRepository;

    private String success = "{\"message\":\"Success\"}";
    private String failure = "{\"message\":\"Failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {
        if (user == null) return failure;
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User request) {
        // Fetch the existing user by id
        User existingUser = userRepository.findById(id);
        
        // Check if user exists, if not return null or handle error
        if (existingUser == null) {
            return null;  // You could also throw an exception or return a 404 response here
        }
    
        // Update the existing user's details with the values from the request
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());
        existingUser.setType(request.getType());
    
        // Save the updated user back to the repository
        userRepository.save(existingUser);
        
        // Return the updated user
        return existingUser;
    }
    


    @GetMapping("/users/organisation/{orgId}")
    List<User> getUsersByOrganisation(@PathVariable String orgId) {
        return userRepository.findByOrganisation_OrgId(orgId);
    }

    @GetMapping("/users/club/{clubId}")
    List<User> getUsersByClub(@PathVariable int clubId) {
        return userRepository.findByClub_ClubId(clubId);
    }

    @PutMapping("/users/{userId}/organisation/{orgId}")
    String assignOrganisationToUser(@PathVariable int userId, @PathVariable String orgId) {
        User user = userRepository.findById(userId);
        Organisation organisation = organisationRepository.findById(orgId).orElse(null);
        if (user == null || organisation == null) return failure;
        user.setOrganisation(organisation);
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{userId}/club/{clubId}")
    String assignClubToUser(@PathVariable int userId, @PathVariable int clubId) {
        User user = userRepository.findById(userId);
        Club club = clubRepository.findById(clubId).orElse(null);
        if (user == null || club == null) return failure;
        user.setClub(club);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }
}