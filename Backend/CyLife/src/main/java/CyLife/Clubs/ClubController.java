
package CyLife.Clubs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClubController {

    @Autowired
    ClubRepository clubRepository;

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
}
