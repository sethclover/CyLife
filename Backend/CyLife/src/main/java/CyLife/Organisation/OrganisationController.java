package CyLife.Organisation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganisationController {

    @Autowired
    OrganisationRepository organisationRepository;

    private String success = "{\"message\":\"Success\"}";
    private String failure = "{\"message\":\"Failure\"}";

    @GetMapping(path = "/organisations")
    List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    @GetMapping(path = "/organisations/{id}")
    Organisation getOrganisationById(@PathVariable String id) {
        return organisationRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "/organisations")
    String createOrganisation(@RequestBody Organisation organisation) {
        if (organisation == null) return failure;
        organisationRepository.save(organisation);
        return success;
    }

    @PutMapping("/organisations/{id}")
    Organisation updateOrganisation(@PathVariable String id, @RequestBody Organisation request) {
        Organisation organisation = organisationRepository.findById(id).orElse(null);
        if (organisation == null) return null;
        
        // Update the fields of the existing organisation
        if (request.getName() != null) {
            organisation.setName(request.getName());
        }
        if (request.getEmail() != null) {
            organisation.setEmail(request.getEmail());
        }
        if (request.getQualification() != null) {
            organisation.setQualification(request.getQualification());
        }

        organisationRepository.save(organisation);
        return organisation;
    }

    @DeleteMapping(path = "/organisations/{id}")
    String deleteOrganisation(@PathVariable String id) {
        organisationRepository.deleteById(id);
        return success;
    }
}
