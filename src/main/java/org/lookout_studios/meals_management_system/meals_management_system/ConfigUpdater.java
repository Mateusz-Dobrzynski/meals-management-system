package org.lookout_studios.meals_management_system.meals_management_system;

import java.io.*;
import java.util.Properties;
import org.json.JSONObject;

public class ConfigUpdater {
    private static String emailProperty = "spring.mail.username";
    private static String emailPasswordProperty = "spring.mail.password";
    private static String configFilePath = ".config\\.config";
    private static String propertiesFilePath = "src\\main\\resources\\application.properties";
    public static void main(String[] args) {
        
        try {
            // Read the JSON data from .config file
            BufferedReader configReader = new BufferedReader(new FileReader(configFilePath));
            StringBuilder configJson = new StringBuilder();
            String line;
            while ((line = configReader.readLine()) != null) {
                configJson.append(line);
            }
            configReader.close();

            // Parse JSON and extract email
            JSONObject configJsonObject = new JSONObject(configJson.toString());
            String email = configJsonObject.getString("email");
            String emailPassword = configJsonObject.getString("emailPassword");

            // Load application.properties
            FileInputStream propertiesFileInput = new FileInputStream(propertiesFilePath);
            Properties properties = new Properties();
            properties.load(propertiesFileInput);
            propertiesFileInput.close();

            // Update the property
            properties.setProperty(emailProperty, email);
            properties.setProperty(emailPasswordProperty, emailPassword);

            // Save the updated properties back to application.properties
            FileOutputStream propertiesFileOutput = new FileOutputStream(propertiesFilePath);
            properties.store(propertiesFileOutput, null);
            propertiesFileOutput.close();

            //Remove before review
            System.out.println("Updated spring.mail.username with email: " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
