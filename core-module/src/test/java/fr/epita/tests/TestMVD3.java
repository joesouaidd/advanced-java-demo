package fr.epita.tests;

import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import fr.epita.datamodels.Booking;
import fr.epita.services.DatabaseConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class TestMVD3 {

    /**
     * MVD3: A standalone test class to validate the manual initialization and
     * querying of an H2 in-memory database.
     *
     * - Initializes the H2 in-memory database manually using JDBC (Java Database
     * Connectivity) within a `try-with-resources` block.
     * 
     * - Reads and executes the `base.sql` file manually to initialize the database
     * schema and populate initial data.
     * 
     * - Uses Hibernate's `EntityManager` for querying data from the database.
     * 
     * - Fetches and prints facilities, members, and bookings data to validate
     * successful database initialization.
     * 
     * - The persistence configuration is set to `create-drop`, meaning the database
     * schema is dropped after the session ends.
     * 
     * - This test highlights manual database setup and interaction without relying
     * on a Spring-managed `DataSource` bean.
     * 
     * JDBC provides a uniform interface for accessing various databases (e.g.,
     * MySQL, PostgreSQL, Oracle, etc.) using SQL without worrying about the
     * specific database implementation.
     * 
     * 
     * Components:
     * 
     * *DriverManager: Manages database drivers and establishes databaseconnections.
     * *Connection: Represents the connection to a specific database.
     * *Statement: Used to execute SQL queries or updates.
     * *ResultSet: Stores the data retrieved from a database query.
     * 
     * Workflow:
     * 
     * *Load a database driver.
     * *Establish a connection to the database.
     * *Execute SQL statements using Statement or PreparedStatement.
     * *Process the results using ResultSet.
     * *Close the connection to free resources.
     * 
     */

    public static void main(String[] args) {
        System.out.println("Initializing H2 database instance and importing data from base.sql...");

        // just to see the current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Step 1: Initialize the H2 database and connect to it using JDBC

        // loads the JDBC driver
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");

        // Establish a JDBC connection to the H2 in-memory database
        // the checking of the number has been made inside of the try block because the
        // connection is closed after the try block and the data is not available after
        // that
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                Statement statement = connection.createStatement()) {

            // Drop tables if they exist because the test-persistence-unit usually create
            // the table schema because the persistence.xml file has the property
            // hibernate.hbm2ddl.auto set to create-drop
            statement.execute("DROP TABLE IF EXISTS bookings CASCADE");
            statement.execute("DROP TABLE IF EXISTS facilities CASCADE");
            statement.execute("DROP TABLE IF EXISTS members CASCADE");

            // Step 2: Read SQL commands from the base.sql file and execute them
            try (BufferedReader reader = new BufferedReader(
                    new FileReader("./core-module/src/main/resources/base.sql"))) {
                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
                String sql = sqlBuilder.toString(); // The complete SQL script
                statement.execute(sql); // Execute the SQL script
                System.out.println("SQL from base.sql executed successfully.");
            }

            // Step 3: Create the EntityManager for querying data from the database
            // em is in the hibernate namespace
            EntityManager em = DatabaseConfig.getEntityManager();

            // Step 4: Fetch and display all facilities
            System.out.println("Fetching facilities from the database...");
            TypedQuery<Facility> facilityQuery = em.createQuery("SELECT f FROM Facility f", Facility.class);
            List<Facility> facilities = facilityQuery.getResultList();
            facilities.forEach(System.out::println); // Print each facility object

            // Step 5: Fetch and display all members
            System.out.println("Fetching members from the database...");
            TypedQuery<Member> memberQuery = em.createQuery("SELECT m FROM Member m", Member.class);
            List<Member> members = memberQuery.getResultList();
            members.forEach(System.out::println); // Print each member object

            // Step 6: Fetch and display all bookings
            System.out.println("Fetching bookings from the database...");
            TypedQuery<Booking> bookingQuery = em.createQuery("SELECT b FROM Booking b", Booking.class);
            List<Booking> bookings = bookingQuery.getResultList();
            bookings.forEach(System.out::println); // Print each booking object

        } catch (Exception e) {
            // Print the stack trace in case of any errors during execution
            e.printStackTrace();
        } finally {
            // Step 7: Close the EntityManagerFactory to release resources
            DatabaseConfig.closeEntityManagerFactory();
            System.out.println("TestMVD3 execution completed.");
        }
    }
}
