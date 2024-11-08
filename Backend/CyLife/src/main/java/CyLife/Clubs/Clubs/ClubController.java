package CyLife.Clubs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClubController {

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubRequestRepository clubRequestRepository;

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
    String createClub(@RequestBody Club club) {
        if (club == null) return failure;
        clubRepository.save(club);
        return success;
    }

    @PutMapping("/clubs/{id}")
    public Club updateClub(@PathVariable int id, @RequestBody Club request) {
        Club existingClub = clubRepository.findById(id).orElse(null);
        if (existingClub == null) {
            return null;
        }

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
        return existingClub;
    }

    @DeleteMapping(path = "/clubs/{id}")
    String deleteClub(@PathVariable int id) {
        clubRepository.deleteById(id);
        return success;
    }

    @PostMapping(path = "/club-requests")
    public String requestNewClub(@RequestBody ClubRequest clubRequest) {
        if (clubRequest == null) return failure;
        clubRequest.setStatus("PENDING");
        clubRequestRepository.save(clubRequest);
        return success;
    }

    @PutMapping(path = "/club-requests/{id}/status")
    public String updateClubRequestStatus(@PathVariable int id, @RequestParam String status) {
        Optional<ClubRequest> clubRequestOpt = clubRequestRepository.findById(id);
        if (!clubRequestOpt.isPresent()) return "{ \"message\": \"Club request not found.\" }";

        ClubRequest clubRequest = clubRequestOpt.get();
        clubRequest.setStatus(status.toUpperCase());
        clubRequestRepository.save(clubRequest);

        // If approved, create the club in the ClubRepository
        if ("APPROVED".equalsIgnoreCase(status)) {
            Club newClub = new Club(clubRequest.getClubName(), clubRequest.getDescription(), clubRequest.getClubEmail());
            clubRepository.save(newClub);
            return "{ \"message\": \"Your club '" + clubRequest.getClubName() + "' has been approved.\" }";
        } else if ("DECLINED".equalsIgnoreCase(status)) {
            return "{ \"message\": \"Your club '" + clubRequest.getClubName() + "' has been declined.\" }";
        }

        return "{ \"message\": \"Invalid status update.\" }";
    }
}
