package fr.epita.tests;

import fr.epita.config.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestSPR2 {

    private DataSource dataSource;

    @BeforeEach
    public void setup() throws Exception {
        System.out.println("Initializing database and executing SQL files...");

        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the DataSource bean from Spring context
        dataSource = context.getBean(DataSource.class);

    }

    @Test
    public void testMemberCount() throws Exception {
        System.out.println("Starting member count test...");

        // Query the number of members directly using the DataSource
        int memberCount;
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM members")) {

            resultSet.next();
            memberCount = resultSet.getInt("count");
        }

        System.out.println("Member count: " + memberCount);

        // Assert the member count is as expected
        Assertions.assertEquals(30, memberCount, "The member count in the database is incorrect.");
    }
}
