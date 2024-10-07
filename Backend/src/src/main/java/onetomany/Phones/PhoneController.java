package onetomany.Phones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneController {

    @Autowired
    PhoneRepository phoneRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/phones")
    List<Phone> getAllPhones(){
        return phoneRepository.findAll();
    }

    @GetMapping(path = "/phones/{id}")
    Phone getPhoneById( @PathVariable int id){
        return phoneRepository.findById(id);
    }

    @PostMapping(path = "/phones")
    String createPhone(Phone phone){
        if (phone == null)
            return failure;
        phoneRepository.save(phone);
        return success;
    }

    @PutMapping("/phones/{id}")
    Phone updatePhone(@PathVariable int id, @RequestBody Phone request){
        Phone phone = phoneRepository.findById(id);
        if(phone == null)
            return null;
        phoneRepository.save(request);
        return phoneRepository.findById(id);
    } 
      
}
