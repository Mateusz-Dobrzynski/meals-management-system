package org.lookout_studios.meals_management_system.meals_management_system;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {
    
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("Example <from@email.com>");
        
        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);
    }
}
