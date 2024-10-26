package onetomany.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import onetomany.Clubs.Club;
import onetomany.Clubs.ClubRepository;
import onetomany.Organisation.Organisation;
import onetomany.Organisation.OrganisationRepository;

//Imports Dhvani Added
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//Only these two above

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Endpoint to get all users
    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User newUser) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if the user already exists by email or username
            if (userRepository.existsByEmail(newUser.getEmail()) ||
                userRepository.existsByUsername(newUser.getUsername())) {
                response.put("message", "User already exists.");
                response.put("status", "409");  // Conflict
            } else {
                // Save the new user
                userRepository.save(newUser);
                response.put("message", "User registered successfully.");
                response.put("status", "201");  // Created
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("status", "500");
        }

        return response;
    }





    @PutMapping("/update/{username}")
    public Map<String, Object> updateUser(
        @PathVariable String username, @RequestBody User updatedUser) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if the user exists
            User existingUser = userRepository.findByUsername(username);
            if (existingUser == null) {
                response.put("message", "User not found with username: " + username);
                response.put("status", "404");
                return response;
            }

            // Update the user's details
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setType(updatedUser.getType());

            // Save the updated user
            userRepository.save(existingUser);

            response.put("message", "User updated successfully.");
            response.put("status", "200");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("status", "500");
        }

        return response;
    }




    @PutMapping("/editusername/{newusername}")
    public Map<String, String> changeUserName(@RequestBody User user, @PathVariable String newUsername){
        Map<String, String> response = new HashMap<>();
        boolean testForName = userRepository.existsByUsername(newUsername);
        System.out.println("testForName");
        if(testForName){
            response.put("message", "The username \""+newUsername+"\" is already taken");
            response.put("status", "409");
            return response;
        }
        String oldUsername = user.getName();
        User existingUser = userRepository.findByUsername(oldUsername);
        if(existingUser != null){
            response.put("message","Username updated successfully from "+oldUsername+" to "+newUsername);
            response.put("status","200");
            existingUser.setName(newUsername);
            userRepository.save(existingUser);
        } else {
            response.put("message", "User not found with username: " + oldUsername);
            response.put("status", "404");
        }
        return response;
    }


    @Transactional
    @DeleteMapping("/delete/{username}")
    public Map<String, Object> deleteUser(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (!userRepository.existsByUsername(username)) {
                response.put("message", "User not found with username: " + username);
                response.put("status", "404");
            } else {
                userRepository.deleteByUsername(username);
                response.put("message", "User deleted successfully.");
                response.put("status", "200");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("status", "500");
        }

        return response;
    }



    @GetMapping("/login/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable String username) {

        Map<String, Object> response = new HashMap<>();

        // Retrieve the user directly using your findByUid method
        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            // If the user exists, add the user details and status to the response
            response.put("user", existingUser);
            response.put("status", "200"); // HTTP 200 OK
        } else {
            // If the user does not exist, return a 404 message
            response.put("message", "User not found with username: " + username);
            response.put("status", "404"); // HTTP 404 Not Found
        }

        // Return the JSON response
        return response;
    }

}