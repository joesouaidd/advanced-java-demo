package fr.epita.tests;

import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import fr.epita.datamodels.Booking;
import fr.epita.services.DatabaseConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJUN1 {

    @BeforeEach
    public void setup() throws Exception {
        System.out.println("Initializing database and executing base.sql...");

        // Initialize the EntityManagerFactory
        // Here we have now an active connection to the H2 database (connection numbers
        // = 1)
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");
        // it will automatically create the database schema based on the entities
        // because of the hibernate.hbm2ddl.auto property in the persistence.xml file

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                Statement statement = connection.createStatement()) {

            // Here we have now an active connection to the H2 database (connection n. = 2)
            // Drop tables if they exist
            statement.execute("DROP TABLE IF EXISTS bookings CASCADE;");
            statement.execute("DROP TABLE IF EXISTS facilities CASCADE;");
            statement.execute("DROP TABLE IF EXISTS members CASCADE;");

            // Load SQL from base.sql
            try (BufferedReader reader = new BufferedReader(
                    new FileReader("./src/main/resources/base.sql"))) {
                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
                String sql = sqlBuilder.toString();
                statement.execute(sql);
                System.out.println("SQL from base.sql executed successfully.");
            }
        }

        // Check the number of active connections in the pool
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                Statement statement = connection.createStatement()) {

            // Here we have now an active connection to the H2 database (connection n. = 2)
            System.out.println("Checking active connections in the H2 pool...");
            var resultSet = statement.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.SESSIONS");
            if (resultSet.next()) {
                int activeConnections = resultSet.getInt(1);
                System.out.println("Active connections in the pool: " + activeConnections);
            }
        }
    }

    @Test
    public void testFetchData() {
        System.out.println("Starting TestJUN1 test...");

        // Fetch data using JPA
        EntityManager em = DatabaseConfig.getEntityManager();

        // Fetch and assert facilities
        TypedQuery<Facility> facilityQuery = em.createQuery("SELECT f FROM Facility f", Facility.class);
        List<Facility> facilities = facilityQuery.getResultList();
        System.out.println("Facilities fetched: " + facilities.size());
        assertEquals(9, facilities.size(), "Expected 9 facilities in the database");

        // Fetch and assert members
        TypedQuery<Member> memberQuery = em.createQuery("SELECT m FROM Member m", Member.class);
        List<Member> members = memberQuery.getResultList();
        System.out.println("Members fetched: " + members.size());
        assertEquals(30, members.size(), "Expected 30 members in the database");

        // Fetch and assert bookings
        TypedQuery<Booking> bookingQuery = em.createQuery("SELECT b FROM Booking b", Booking.class);
        List<Booking> bookings = bookingQuery.getResultList();
        System.out.println("Bookings fetched: " + bookings.size());
        assertEquals(0, bookings.size(), "Expected 0 bookings in the database");
    }
}
