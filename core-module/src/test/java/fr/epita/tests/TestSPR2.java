package fr.epita.tests;

import fr.epita.config.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
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

        // Drop and recreate the schema
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS bookings CASCADE;");
            statement.execute("DROP TABLE IF EXISTS facilities CASCADE;");
            statement.execute("DROP TABLE IF EXISTS members CASCADE;");

            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Load SQL files
            loadSQLFile(statement, "./src/main/resources/create-members.sql");
            loadSQLFile(statement, "./src/main/resources/create-facilities.sql");
            loadSQLFile(statement, "./src/main/resources/create-bookings.sql");
            loadSQLFile(statement, "./src/main/resources/insert-members.sql");
            loadSQLFile(statement, "./src/main/resources/insert-facilities.sql");
            loadSQLFile(statement, "./src/main/resources/insert-bookings.sql");

            connection.commit();

            System.out.println("SQL data loaded successfully.");
        }
    }

    private void loadSQLFile(Statement statement, String filePath) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }

            String sql = sqlBuilder.toString();
            statement.execute(sql);
            System.out.println("Loaded SQL from: " + filePath);
        }
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
