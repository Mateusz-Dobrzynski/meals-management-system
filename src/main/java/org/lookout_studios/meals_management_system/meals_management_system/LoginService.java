package org.lookout_studios.meals_management_system.meals_management_system;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping; /* TO-DO: For tests only. Remove later.*/

@RestController
public class LoginService {
    
    /* TO-DO: For tests only. Remove later. */
    @GetMapping("api/test/{resourceId}")
    public String getString(@PathVariable String resourceId) {
        return "Hello website! Reource ID: " + resourceId;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public void receiveData(@RequestBody LoginForm loginForm) {
        System.out.println("Here is the receive profile: " + loginForm);
    }
}

class LoginForm {

    private String email;

    private String password;

    public LoginForm() {

    }
}