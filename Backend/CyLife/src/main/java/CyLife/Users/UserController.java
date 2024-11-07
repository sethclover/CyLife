package CyLife.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import CyLife.Clubs.Club;
import CyLife.Clubs.ClubRepository;
import CyLife.Organisation.Organisation;
import CyLife.Organisation.OrganisationRepository;

import org.springframework.transaction.annotation.Transactional;
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

    // Endpoint to get a user by id
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id);

            if (user != null) {
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint to update user by id
    @PutMapping("/update/{id}")
    public Map<String, Object> updateUser(
            @PathVariable int id, @RequestBody User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = userRepository.findById(id);
            if (existingUser == null) {
                response.put("message", "User not found with id: " + id);
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

    @Transactional
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!userRepository.existsById(id)) {
                response.put("message", "User not found with id: " + id);
                response.put("status", "404");
            } else {
                userRepository.deleteById(id);
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


    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User newUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userRepository.existsByEmail(newUser.getEmail())) {
                response.put("message", "User already exists.");
                response.put("status", "409");
            } else {
                if (newUser.getType() == null) {
                    newUser.setType(User.UserType.STUDENT); // Set default to STUDENT
                }
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


    @PutMapping("/update/{email}")
    public Map<String, Object> updateUser(
        @PathVariable String email, @RequestBody User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = userRepository.findByEmail(email);
            if (existingUser == null) {
                response.put("message", "User not found with email: " + email);
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


    @Transactional
    @DeleteMapping("/delete/{email}")
    public Map<String, Object> deleteUser(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!userRepository.existsByEmail(email)) {
                response.put("message", "User not found with email: " + email);
                response.put("status", "404");
            } else {
                userRepository.deleteByEmail(email);
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
            response.put("userID", user.getUserId());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
