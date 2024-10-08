package onetomany.Organisation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganisationController {

    @Autowired
    OrganisationRepository organisationRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

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
        organisation.setName(request.getName());
        organisation.setEmail(request.getEmail());
        organisation.setQualification(request.getQualification());

        organisationRepository.save(organisation);
        return organisation;
    }

    @DeleteMapping(path = "/organisations/{id}")
    String deleteOrganisation(@PathVariable String id) {
        organisationRepository.deleteById(id);
        return success;
    }
}
