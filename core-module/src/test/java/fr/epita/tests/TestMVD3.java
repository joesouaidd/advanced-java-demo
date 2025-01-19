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

    public static void main(String[] args) {
        System.out.println("Initializing H2 database instance and importing data from base.sql...");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Step 1: Initialize the H2 database and connect to it using JDBC
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                Statement statement = connection.createStatement()) {

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
