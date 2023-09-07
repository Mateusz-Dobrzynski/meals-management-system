package org.lookout_studios.meals_management_system.meals_management_system;

import java.io.FileReader;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@ComponentScan("org.lookout_studios.meals_management_system.meals_management_system")
public class MailConfig {
    static String configFilePath = ".config\\.config";
    static String configSenderEmail = "email";
    static String configEmailPassword = "emailPassword";
    static String configSmtpHost = "smtphost";
    static String configSmtpPort = "smtpport";


    @Bean
    public JavaMailSender javaMailSender() throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Read the configuration from the JSON file
        try {        
            JSONParser jsonParser = new JSONParser();
            FileReader jsonFileDataReader = new FileReader(configFilePath);
            JSONObject saveJsonData = (JSONObject) jsonParser.parse(jsonFileDataReader);
            String senderEmail = (String) saveJsonData.get(configSenderEmail);
            String emailPassword = (String) saveJsonData.get(configEmailPassword);
            String smtpHost = (String) saveJsonData.get(configSmtpHost);
            String smtpPort = (String) saveJsonData.get(configSmtpPort);

            
            mailSender.setHost(smtpHost);
            mailSender.setPort(Integer.parseInt(smtpPort));
            mailSender.setUsername(senderEmail);
            mailSender.setPassword(emailPassword);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true"); //Remove this line before merging
        } catch (Exception exception) {
            throw exception;
        }
        return mailSender;
    }
}
