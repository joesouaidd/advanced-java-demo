package fr.epita.tests;

import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import fr.epita.datamodels.Booking;
import fr.epita.services.DatabaseConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class TestJUN1 {

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Initializing H2 database instance and importing data from base.sql...");

        // Initialize the EntityManagerFactory for JPA-based interactions
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");

        // Establish a JDBC connection to the H2 in-memory database
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                Statement statement = connection.createStatement()) {

            // Read SQL commands from the base.sql file
            try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/base.sql"))) {
                StringBuilder sqlBuilder = new StringBuilder();
                String line;

                // Append each line of the file to build the full SQL script
                while ((line = reader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }

                // Execute the constructed SQL script to populate the database
                String sql = sqlBuilder.toString();
                statement.execute(sql);
                System.out.println("SQL from base.sql executed successfully.");
            }
        }
    }

    @AfterEach
    public void tearDown() {
        // Close the EntityManagerFactory to release all associated resources
        DatabaseConfig.closeEntityManagerFactory();
        System.out.println("Database instance closed.");
    }

    @Test
    public void testFetchFacilities() {
        // Retrieve the EntityManager for querying the database
        EntityManager em = DatabaseConfig.getEntityManager();
        System.out.println("Fetching facilities from the database...");

        // Use JPQL to fetch all Facility entities
        TypedQuery<Facility> facilityQuery = em.createQuery("SELECT f FROM Facility f", Facility.class);
        List<Facility> facilities = facilityQuery.getResultList();

        // Print each fetched Facility object to the console
        facilities.forEach(System.out::println);

        // Assert that the fetched Facility list size matches the expected value
        Assertions.assertEquals(9, facilities.size(), "The number of facilities retrieved is incorrect.");
    }

    @Test
    public void testFetchMembers() {
        // Retrieve the EntityManager for querying the database
        EntityManager em = DatabaseConfig.getEntityManager();
        System.out.println("Fetching members from the database...");

        // Use JPQL to fetch all Member entities
        TypedQuery<Member> memberQuery = em.createQuery("SELECT m FROM Member m", Member.class);
        List<Member> members = memberQuery.getResultList();

        // Print each fetched Member object to the console
        members.forEach(System.out::println);

        // Assert that the fetched Member list size matches the expected value
        Assertions.assertEquals(30, members.size(), "The number of members retrieved is incorrect.");
    }

    @Test
    public void testFetchBookings() {
        // Retrieve the EntityManager for querying the database
        EntityManager em = DatabaseConfig.getEntityManager();
        System.out.println("Fetching bookings from the database...");

        // Use JPQL to fetch all Booking entities
        TypedQuery<Booking> bookingQuery = em.createQuery("SELECT b FROM Booking b", Booking.class);
        List<Booking> bookings = bookingQuery.getResultList();

        // Print each fetched Booking object to the console
        bookings.forEach(System.out::println);

        // Assert that the fetched Booking list size matches the expected value
        // Replace 0 with the actual expected size of the bookings list
        Assertions.assertEquals(0, bookings.size(), "The number of bookings retrieved is incorrect.");
    }
}
