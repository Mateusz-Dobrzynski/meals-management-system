package org.lookout_studios.meals_management_system.meals_management_system;

import java.io.FileReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegistrationService {   
    private String invalidEmailMessage = "Invalid email address";
    private String invalidPasswordMessage = "Invalid password";
    private String alreadyRegisteredMessage = "This user already exists";
    private String validPasswordPattern = ".{8,}";
    private String validEmailPattern = "^[\\w+-]+(\\.[\\w+-]+)*[\\.]?[a-zA-Z0-9]@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static String emailSubject = "Meals Managament System E-mail Verification";
    private static String configFilePath = ".config\\.config";
    private static String configProtocol = "protocol";
    private static String configDomain = "domain";
    private static String configPort = "port";
     //TO-DO: The verification link must be created automatically.
    /* "http://localhost:8080/verify?userId=24&registrationToken=null" */
    Logger log = LoggerFactory.getLogger(RegistrationService.class);

    /**
     * Handles /register requests by checking validity of provided data, registering
     * new users in the database and sending confirmation emails.
     * 
     * @param user User object defined in a request body
     * @return JSON response with registration token
     * @throws Exception
     */
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody User user) throws Exception {
        String emailMessage = "To verify your e-mail click on the provided link: " + generateVerificationUrl(user);
        String userEmail = user.getEmail();
        log.info(String.format(
                "New registration request for user with email '%s'",
                userEmail));
        log.info(String.format(
                "Checking if '%s' is a valid email",
                userEmail));
        if (!emailCheck(userEmail)) {
            log.info(String.format("Email '%s' is invalid", userEmail));
            return new ResponseEntity<String>(
                    new ResponseBody(HttpStatus.BAD_REQUEST, invalidEmailMessage).getResponseBody(),
                    HttpStatus.BAD_REQUEST);
        }
        if (!passwordCheck(user.getPassword())) {
            log.info(String.format("Password provided by %s is invalid", userEmail));
            return new ResponseEntity<String>(
                    new ResponseBody(HttpStatus.BAD_REQUEST, invalidPasswordMessage).getResponseBody(),
                    HttpStatus.BAD_REQUEST);
        }
        DatabaseService databaseService = new DatabaseService();
        try {
            log.info(String.format(
                    "Checking if %s is already registered",
                    userEmail));
            boolean isRegistered = databaseService.isUserRegistered(userEmail);
            if (isRegistered) {
                log.info(String.format(
                        "User with email %s is already registered",
                        userEmail));
                return new ResponseEntity<String>(
                        new ResponseBody(
                                HttpStatus.BAD_REQUEST,
                                alreadyRegisteredMessage).getResponseBody(),
                        HttpStatus.BAD_REQUEST);
            }
            log.info(String.format(
                    "%s is not registered",
                    userEmail));
        } catch (Exception exception) {
                throw exception;
        }
        log.info(String.format(
                "Registering new user with email '%s'",
                userEmail));
        databaseService.registerNewUser(user);
        // TO-DO: Send email with registration link containing the token
        log.info(String.format(
                "User with email %s registered successfully",
                userEmail));
        sendVerificationEmail(user.getEmail(), emailSubject, emailMessage);
        log.info(String.format(
            "Verification e-mail has been sent"));
        return new ResponseEntity<String>(
                new ResponseBody(HttpStatus.OK).getResponseBody(), HttpStatus.OK);
    }

    /**
     * Checks if a given string is a valid email address
     * 
     * @param email An email address to be checked
     * @return True if a string is a valid email address, false if it isn't
     */
    public boolean emailCheck(String email) {
        boolean match;
        try {
            Pattern pattern = Pattern.compile(validEmailPattern);
            Matcher matcher = pattern.matcher(email);
            match = matcher.find();
        } catch (NullPointerException exception) {
            return false;
        } catch (Exception exception) {
            throw exception;
        }
        return match;
    }

    /**
     * Checks if a given password is valid i.e. is at least 8 characters long
     * 
     * @param password Password to be checked
     * @return True if a password is valid, false if it's invalid
     */
    public boolean passwordCheck(String password) {
        boolean match;
        try {
            Pattern pattern = Pattern.compile(validPasswordPattern);
            Matcher matcher = pattern.matcher(password);
            match = matcher.find();
        } catch (NullPointerException exception) {
            return false;
        } catch (Exception exception) {
            throw exception;
        }
        return match;
    }

    //TO-DO: Remove dependencies from application.properties
    //       Rebuild ConfigUpdater

    /**
     * Sends an e-mail message so user can be verified
     * 
     * @param email E-mail address to which the message will be sent
     * @param emailSubject A subject of e-mail
     * @param message A message containing verification link
     */
    public void sendVerificationEmail(String email, String emailSubject, String message) {
        try (AnnotationConfigApplicationContext 
        context = new AnnotationConfigApplicationContext(MailConfig.class)) {
            EmailService emailService = context.getBean(EmailService.class);

            String to = email;
            String subject = emailSubject;
            String text = message;

            emailService.sendEmail(to, subject, text);
        } catch (BeansException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * com.
     * @throws Exception
     */
    private String generateVerificationUrl(User user) throws Exception {
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader jsonFileDataReader = new FileReader(configFilePath);
            JSONObject configData = (JSONObject) jsonParser.parse(jsonFileDataReader);
    
            // Retrieve values from the JSON config
            String protocol = (String) configData.get(configProtocol);
            String domain = (String) configData.get(configDomain);
            int port = ((Long) configData.get(configPort)).intValue();
     // You can set this to a value if needed
            String authorisation = null;
            String path = "/verify";
            String fragment = null;
            String id = String.valueOf(user.getUserId());
    
            // Use the user's ID in the query parameter
            String query = "userId=" + id + "&registrationToken=" + user.generateRegistrationToken();
    
            // Construct the URI using the config values
            URI uri = new URI(protocol, authorisation, domain, port, path, query, fragment);
    
            return uri.toString();
        } catch (Exception exception) {
            throw exception;
        }
    }
}
