package org.lookout_studios.meals_management_system.meals_management_system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    
    private EmailService mailService;

    public EmailController(EmailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/sendemail")
    public String sendEmail() {
        mailService.sendSimpleEmail("Odbiorca <odbiorca@maila.pl>", "Test e-mail", "Testing email functionality");

        return "E-mail sent!";
    }
}
