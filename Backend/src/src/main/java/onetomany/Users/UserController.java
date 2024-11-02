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



    @GetMapping("/user/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "User not found with username: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User newUser) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (userRepository.existsByEmail(newUser.getEmail()) ||
                userRepository.existsByUsername(newUser.getUsername())) {
                response.put("message", "User already exists.");
                response.put("status", "409");
            } else {
                userRepository.save(newUser);
                response.put("message", "User registered successfully.");
                response.put("status", "201");
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
            User existingUser = userRepository.findByUsername(username);
            if (existingUser == null) {
                response.put("message", "User not found with username: " + username);
                response.put("status", "404");
                return response;
            }
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setType(updatedUser.getType());

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



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            response.put("message", "Email or password is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        User user = userRepository.findByEmail(email.trim());

        if (user != null && user.getPassword().equals(password.trim())) {
            response.put("message", "Login successful");
            response.put("userType", user.getType());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


}