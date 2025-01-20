package fr.epita.tests;

import fr.epita.datamodels.Facility;
import fr.epita.services.data.FacilityJPADAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import fr.epita.config.AppConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFacilityJPADAO {

    private ApplicationContext context;
    private FacilityJPADAO facilityDAO;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Initializing Spring Application Context and cleaning database...");

        // Initialize Spring context
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve DAOs from Spring context
        facilityDAO = context.getBean(FacilityJPADAO.class);

        // Clean up database to ensure tests run in isolation
        try (Connection connection = context.getBean(DataSource.class).getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM bookings");
            statement.execute("DELETE FROM facilities");
            statement.execute("DELETE FROM members");
            System.out.println("Database cleaned successfully.");
        }
    }

    @Test
    public void testAddAndListFacility() {
        System.out.println("Starting test: testAddAndListFacility");

        // Step 1: Create and add a Facility
        Facility facility = new Facility("Tennis Court", new BigDecimal(5), new BigDecimal(25), new BigDecimal(10000),
                new BigDecimal(200));
        facilityDAO.add(facility);

        // Step 2: Fetch all Facilities
        List<Facility> facilities = facilityDAO.listAll();

        // Step 3: Validate the results
        assertEquals(1, facilities.size(), "The number of facilities retrieved is incorrect");
        assertEquals("Tennis Court", facilities.get(0).getName(), "The retrieved facility's name is incorrect");

        System.out.println("Test testAddAndListFacility completed successfully.");
    }
}
