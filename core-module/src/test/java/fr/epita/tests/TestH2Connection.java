package fr.epita.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestH2Connection {

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws Exception {
        // Load H2 Database Driver
        Class.forName("org.h2.Driver");

        // Establish a connection to the H2 database
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        connection = DriverManager.getConnection(jdbcUrl, "sa", "");

        // Create a test table
        String createTableSQL = "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50))";
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
        statement.close();
    }

    @Test
    public void testInsertAndQuery() throws Exception {
        // Insert a sample record
        String insertSQL = "INSERT INTO test_table (id, name) VALUES (1, 'Test')";
        Statement statement = connection.createStatement();
        statement.execute(insertSQL);

        // Query the table
        String querySQL = "SELECT * FROM test_table";
        ResultSet resultSet = statement.executeQuery(querySQL);

        // Assert the results
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            assertEquals(1, id);
            assertEquals("Test", name);
        }

        statement.close();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // Drop the test table and close the connection
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE test_table");
        statement.close();

        connection.close();
    }
}
