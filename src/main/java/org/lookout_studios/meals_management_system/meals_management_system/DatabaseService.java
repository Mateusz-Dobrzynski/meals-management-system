package org.lookout_studios.meals_management_system.meals_management_system;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class establishing connection with database.
 * 
 * @author Hubert Borysowski
 */
public class DatabaseService {
    static String connectionInstanceName = "com.mysql.cj.jdbc.Driver";
    static String jsonConfigFilePath = ".config\\.config";
    static String jsonUrlKey = "databaseJdbcUrl";
    static String jsonUsernameKey = "databaseUsername";
    static String jsonPasswordKey = "databasePassword";
    static String executeQueryExceptionReason = "Failed to execute query";

    Logger log = LoggerFactory.getLogger(DatabaseService.class);

    /**
     * Creates an SQL connection with a database,
     * executes a SELECT query and closes the connection.
     * 
     * @param selectQuery mySQL SELECT query
     * @return Result of the query
     * @param connection Connection with the database
     *                   established with establishConnection();
     * @throws Exception
     */
    public ResultSet executeSelectQuery(String selectQuery, Connection connection) throws Exception {
        /*
         * Create JSONParser object, so you can read configuration data from JSON file.
         */
        ResultSet result = null;
        try {
            log.debug(String.format("Executing query %s", selectQuery));
            Statement statement = connection.createStatement();
            result = statement.executeQuery(selectQuery);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw exception;
        }
        return result;
    }

    /**
     * 
     * @param query      Non-SELECT mySQL query
     * @param connection A connection object created with establishConnection()
     *                   method
     * @return A boolean representing database response. See
     *         java.sql.Statement.execute() for further details
     * @throws Exception
     */
    public boolean executeOtherQuery(String query, Connection connection) throws Exception {
        boolean result = false;
        try {
            log.debug(String.format("Executing query %s", query));
            Statement statement = connection.createStatement();
            result = statement.execute(query);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw exception;
        }
        return result;
    }

    /**
     * Reads credentials from a config file and establishes
     * a connection with a database using them.
     * 
     * @return A Connection object ready to be used
     */
    private Connection establishConnection() {
        Connection accessConnection = null;
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader jsonFileDataReader = new FileReader(jsonConfigFilePath);
            JSONObject saveJsonData = (JSONObject) jsonParser.parse(jsonFileDataReader);
            String databaseJdbcUrl = (String) saveJsonData.get(jsonUrlKey);
            String databaseUsername = (String) saveJsonData.get(jsonUsernameKey);
            String databasePassword = (String) saveJsonData.get(jsonPasswordKey);
            Class.forName(connectionInstanceName);
            accessConnection = DriverManager.getConnection(
                    databaseJdbcUrl,
                    databaseUsername,
                    databasePassword);
        } catch (FileNotFoundException fileNotFoundError) {
            System.out.println(fileNotFoundError);
        } catch (IOException ioError) {
            System.out.println(ioError);
        } catch (Exception mysqlError) {
            System.out.println(mysqlError);
        }
        return accessConnection;
    }

    /**
     * Checks if a user with a given email is present in the database
     * 
     * @param email of a user
     * @return true if an email is found in the database, false if it isn't
     * @throws Exception
     */
    public boolean isUserRegistered(String email) throws Exception {
        Connection connection = establishConnection();
        try {
            ResultSet result = executeSelectQuery(
                    String.format(
                            "SELECT u.email FROM users u WHERE u.email = \"%s\";",
                            email),
                    connection);
            if (!result.next()) {
                connection.close();
                return false;
            }
        } catch (Exception exception) {
            throw exception;
        }
        connection.close();
        return true;
    }

    /**
     * Registers new user in the database
     * 
     * @param user A user object representing a user to be registered
     * @throws Exception
     */
    public void registerNewUser(User user) throws Exception {
        Connection connection = establishConnection();
        try {
            executeOtherQuery(
                    String.format(
                            "INSERT INTO users (email, password, isVerified, registrationToken) VALUES (\"%s\", %d, false, \"%s\");",
                            user.getEmail(), user.getPasswordHash(), user.getRegistrationToken()),
                    connection);
        } catch (Exception exception) {
            throw exception;
        }
        connection.close();
    }

    public boolean verifyRegistrationToken(int userId, String registrationToken) throws Exception {
        Connection connection = establishConnection();
        try {
            ResultSet result = executeSelectQuery(
                    String.format(
                            "SELECT u.registrationToken FROM users u WHERE userId = \"%s\"",
                            userId),
                    connection);
            if (!result.next()) {
                log.info(String.format("User with id %d does not exist", userId));
                connection.close();
                return false;
            }
            String foundToken = result.getString("registrationToken");
            if (!foundToken.equals(registrationToken)) {
                log.info(String.format(
                        "Tokens '%s' and '%s' do not match",
                        foundToken,
                        registrationToken));
                connection.close();
                return false;
            }
        } catch (Exception exception) {
            throw exception;
        }
        connection.close();
        return true;
    }

    public void markUserAsVerified(int userId) throws Exception {
        Connection connection = establishConnection();
        try {
            executeOtherQuery(
                    String.format(
                            "UPDATE users SET isVerified = true WHERE userId = %d",
                            userId),
                    connection);
        } catch (Exception exception) {
            throw exception;
        }
        connection.close();
    }
}