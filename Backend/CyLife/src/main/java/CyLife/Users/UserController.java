package CyLife.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import CyLife.Clubs.ClubDTO;
import CyLife.Events.Event;
import CyLife.Events.EventRepository;
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
    ClubRepository clubRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getType().toString()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
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
    @PutMapping("/update/byId/{id}")
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
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getType() != null) {
                existingUser.setType(updatedUser.getType());
            }

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

    @PutMapping("/joinClub/{studentId}/{clubId}")
    public ResponseEntity<Map<String, Object>> joinClub(@PathVariable int studentId, @PathVariable int clubId) {
        Map<String, Object> response = new HashMap<>();
        try {

            User user = userRepository.findById(studentId);
            if (user == null) {
                response.put("message", "User not found with id: " + studentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Club club = clubRepository.findById(clubId).orElse(null);
            if (club == null) {
                response.put("message", "Club not found with id: " + clubId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (user.getClubs().contains(club)) {
                response.put("message", "User is already a member of this club.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.getClubs().add(club);
            userRepository.save(user);

            response.put("message", "User successfully joined the club.");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{userId}/clubs")
    public ResponseEntity<Map<String, Object>> getUserClubs(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Map clubs to ClubDTOs
            Set<ClubDTO> clubs = user.getClubs().stream()
                    .map(club -> new ClubDTO(club.getClubId(), club.getClubName(), club.getDescription(), club.getClubEmail()))
                    .collect(Collectors.toSet());

            response.put("clubs", clubs);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/joinEvent/{userId}/{eventId}")
    public ResponseEntity<Map<String, Object>> joinEvent(@PathVariable int userId, @PathVariable int eventId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                response.put("message", "Event not found with id: " + eventId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (user.getEvents().contains(event)) {
                response.put("message", "User is already attending this event.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.getEvents().add(event);
            userRepository.save(user);

            response.put("message", "User successfully joined the event.");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{userId}/events")
    public ResponseEntity<Map<String, Object>> getUserEvents(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("events", user.getEvents());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/leaveClub/{userId}/{clubId}")
    public ResponseEntity<Map<String, Object>> leaveClub(@PathVariable int userId, @PathVariable int clubId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Find the user
            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Find the club
            Club club = clubRepository.findById(clubId).orElse(null);
            if (club == null) {
                response.put("message", "Club not found with id: " + clubId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Check if the user is part of the club
            if (!user.getClubs().contains(club)) {
                response.put("message", "User is not a member of this club.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Remove the club from the user's list of clubs
            user.getClubs().remove(club);
            userRepository.save(user);

            response.put("message", "User successfully left the club.");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
