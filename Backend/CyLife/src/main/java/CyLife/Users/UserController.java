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
                return ResponseEntity.ok(response); // Branch 1: User exists
            } else {
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Branch 2: User not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Branch 3: Exception
        }
    }


    @PutMapping("/update/byId/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id);
            if (user == null) {
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Branch 1: User not found
            }

            updates.forEach((key, value) -> {
                switch (key) {
                    case "name": user.setName((String) value); break; // Branch 2: Update name
                    case "email": user.setEmail((String) value); break; // Branch 3: Update email
                    case "password": user.setPassword((String) value); break; // Branch 4: Update password
                    case "type": user.setType(User.UserType.valueOf((String) value)); break; // Branch 5: Update type
                    default: break; // Branch 6: Ignore invalid fields
                }
            });

            userRepository.save(user);
            response.put("message", "User updated successfully.");
            return ResponseEntity.ok(response); // Branch 7: Success
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Branch 8: Exception
        }
    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!userRepository.existsById(id)) {
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                userRepository.deleteById(id);
                response.put("message", "User deleted successfully.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User newUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userRepository.existsByEmail(newUser.getEmail())) {
                response.put("message", "User already exists.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
            } else {
                User savedUser = userRepository.save(newUser); // Save and retrieve the saved user
                response.put("message", "User registered successfully.");
                response.put("userId", savedUser.getUserId()); // Include userId in the response
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 Internal Server Error
        }
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

    @PutMapping("/leaveClub/{userId}/{clubId}")
    public ResponseEntity<Map<String, Object>> leaveClub(@PathVariable int userId, @PathVariable int clubId) {
        Map<String, Object> response = new HashMap<>();
        try {

            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Club club = clubRepository.findById(clubId).orElse(null);
            if (club == null) {
                response.put("message", "Club not found with id: " + clubId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (!user.getClubs().contains(club)) {
                response.put("message", "User is not a member of this club.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

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

    @PutMapping("/user/{id}/changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable int id, @RequestBody Map<String, String> passwords) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id);
            if (user == null) {
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                response.put("message", "Both old and new passwords are required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!user.getPassword().equals(oldPassword)) {
                response.put("message", "Old password is incorrect.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            user.setPassword(newPassword);
            userRepository.save(user);
            response.put("message", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/checkMembershipStatus/{userId}/{clubId}")
    public ResponseEntity<Map<String, Object>> checkMembershipStatus(
            @PathVariable int userId, @PathVariable int clubId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                response.put("message", "User not found with id: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Club club = clubRepository.findById(clubId).orElse(null);
            if (club == null) {
                response.put("message", "Club not found with id: " + clubId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            boolean isMember = user.getClubs().contains(club);
            response.put("isMember", isMember);
            response.put("message", isMember ? "User is a member of the club." : "User is not a member of the club.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
