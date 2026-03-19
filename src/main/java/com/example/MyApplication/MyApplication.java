package org.demo.MyApplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MyApplication {

    @PostMapping
    @RequestMapping("/fridge")
    String home(@RequestParam(required = true) String id) {
        String connectionUrl = "jdbc:mysql://localhost:3306/fridge?serverTimezone=UTC";
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = new byte[16];
        ByteBuffer.wrap(uuidBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        String query_add = "INSERT INTO fridges (id, user_id) VALUES(?, ?)";

        try {
            Connection conn = DriverManager.getConnection(connectionUrl, "root", "root");
            PreparedStatement ps = conn.prepareStatement(query_add);
            ps.setString(1, uuid.toString());
            ps.setString(2, id);
            ResultSet rs = ps.executeQuery();
        } catch (SQLException e) {

        }

        return uuid.toString();
    }

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(MyApplication.class);
        SpringApplication.run(MyApplication.class, args);

        String query = "SELECT * FROM users";
        String connectionUrl = "jdbc:mysql://localhost:3306/fridge?serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "root"); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password_hash = rs.getString("password_hash");
                logger.info(username);
                logger.info(password_hash);
            }
        } catch (SQLException e) {
            // handle the exception
            logger.error("sql failed: ", e);
        }
    }
}
