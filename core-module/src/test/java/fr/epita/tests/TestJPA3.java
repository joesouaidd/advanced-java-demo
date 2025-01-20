package fr.epita.tests;

import fr.epita.config.AppConfig;
import fr.epita.datamodels.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class TestJPA3 {

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void setup() {
        System.out.println("Initializing EntityManagerFactory and loading SQL data...");

        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the EntityManagerFactory bean from Spring context
        entityManagerFactory = context.getBean(EntityManagerFactory.class);
    }

    @Test
    public void testBookingListSize() {
        System.out.println("Testing booking list size...");

        // Create EntityManager instance
        // The EntityManager is obtained from the EntityManagerFactory and is the
        // primary JPA interface for managing persistence and executing queries.
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            // Use JPA to query all bookings
            TypedQuery<Booking> query = entityManager.createQuery("SELECT b FROM Booking b", Booking.class);
            List<Booking> bookings = query.getResultList();

            // Print the size of the bookings list
            System.out.println("Number of bookings: " + bookings.size());

            // Assert that the bookings list size is 4044
            Assertions.assertEquals(4044, bookings.size(), "The number of bookings is incorrect.");
        } finally {
            // Close the EntityManager
            entityManager.close();
        }
    }
}
