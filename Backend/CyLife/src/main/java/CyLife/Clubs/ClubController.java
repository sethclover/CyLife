
package CyLife.Clubs;


import java.util.List;

import CyLife.Users.User;
import CyLife.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class ClubController {

    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ClubRequestRepository clubRequestRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"Success\"}";
    private String failure = "{\"message\":\"Failure\"}";

    @GetMapping(path = "/clubs")
    List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @GetMapping(path = "/clubs/{id}")
    Club getClubById(@PathVariable int id) {
        return clubRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "/clubs")
    public ResponseEntity<String> createClub(@RequestBody Club club) {
        if (club.getClubName() == null || club.getClubName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{ \"message\": \"Club name is required.\" }");
        }
        if (club.getDescription() != null && club.getDescription().length() > 5000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{ \"message\": \"Description is too long.\" }");
        }

        // Save the club first
        clubRepository.save(club);

        // Create a user of type CLUB
        User clubUser = new User(
                club.getClubName(),
                club.getClubEmail(),
                "123", // Or generate a secure one
                User.UserType.CLUB
        );

        // Save the user
        User savedUser = userRepository.save(clubUser);

        // Add the user to the club
        club.getUsers().add(savedUser);
        clubRepository.save(club); // Update the club with the new user

        return ResponseEntity.ok("{\"message\":\"Club created and user associated.\"}");
    }

    @GetMapping(path = "/clubId/{clubEmail}")
    public Integer getClubId(@PathVariable String clubEmail) {
        return clubRepository.getClubId(clubEmail);
    }


    @PutMapping("/clubs/{id}")
    public ResponseEntity<Object> updateClub(@PathVariable int id, @RequestBody Club request) {
        Optional<Club> existingClubOpt = clubRepository.findById(id);
        if (!existingClubOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Club not found.\"}");
        }

        Club existingClub = existingClubOpt.get();
        if (request.getClubName() != null) {
            existingClub.setClubName(request.getClubName());
        }
        if (request.getDescription() != null) {
            existingClub.setDescription(request.getDescription());
        }
        if (request.getClubEmail() != null) {
            existingClub.setClubEmail(request.getClubEmail());
        }
        clubRepository.save(existingClub);
        return ResponseEntity.ok(existingClub);
    }


    @DeleteMapping(path = "/clubs/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable int id) {
        if (!clubRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Club not found.\"}");
        }
        clubRepository.deleteById(id);
        return ResponseEntity.ok(success);
    }

    @GetMapping(path = "/club-requests")
    public List<ClubRequest> getAllClubRequests() {
        return clubRequestRepository.findAll();
    }

    @PostMapping(
            path = {"/club-requests"}
    )
    public String requestNewClub(@RequestBody ClubRequest clubRequest) {
        if (clubRequest == null) {
            return this.failure;
        } else {
            clubRequest.setStatus("PENDING");
            this.clubRequestRepository.save(clubRequest);
            return this.success;
        }
    }

    @DeleteMapping(path = "/club-requests/{id}")
    public String deleteClubRequest(@PathVariable int id) {
        if (clubRequestRepository.existsById(id)) {
            clubRequestRepository.deleteById(id);
            return "{ \"message\": \"Club request deleted successfully.\" }";
        } else {
            return "{ \"message\": \"Club request not found.\" }";
        }
    }

    @PutMapping(path = {"/club-requests/{id}/status"})
    public ResponseEntity<String> updateClubRequestStatus(@PathVariable int id, @RequestParam String status) {
        Optional<ClubRequest> clubRequestOpt = clubRequestRepository.findById(id);
        if (!clubRequestOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Club request not found.\"}");
        }

        ClubRequest clubRequest = clubRequestOpt.get();
        clubRequest.setStatus(status.toUpperCase());
        clubRequestRepository.save(clubRequest);

        if ("APPROVED".equalsIgnoreCase(status)) {
            Club newClub = new Club(clubRequest.getClubName(), clubRequest.getDescription(), clubRequest.getClubEmail());
            clubRepository.save(newClub);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Your club '" + clubRequest.getClubName() + "' has been approved.\"}");
        } else if ("DECLINED".equalsIgnoreCase(status)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Your club '" + clubRequest.getClubName() + "' has been declined.\"}");
        } else {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Invalid status update.\"}");
        }
    }



}
