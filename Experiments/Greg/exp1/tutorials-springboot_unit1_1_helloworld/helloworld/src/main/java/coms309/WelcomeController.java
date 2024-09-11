package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

// WelcomeController handles all the requests for welcome messages
@RestController
class WelcomeController {

    // This method returns a default welcome message when the root URL is accessed
    @GetMapping("/")
    public String welcome() {
        return "<html><body style='font-family:Arial; color:blue; font-size:20px;'>"
                + "Welcome to COMS 309! We're excited to have you here."
                + "</body></html>";
    }

    // This method returns a personalized welcome message with dynamic font size and color
    @GetMapping("/{name}")
    public String personalizedWelcome(@PathVariable String name) {
        return "<html><body style='font-family:Arial; color:green; font-size:18px;'>"
                + "Hello " + name + ", welcome to the world of COMS 309! Ready to code?"
                + "</body></html>";
    }
}
