package fr.epita.tests;

import fr.epita.datamodels.Facility;
import fr.epita.services.DatabaseConfig;
import fr.epita.services.data.FacilityJPADAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFacilityJPADAO {

    private FacilityJPADAO facilityDAO;

    @BeforeEach
    public void setUp() {
        System.out.println("Initializing H2 database instance for Facility tests...");
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");
        facilityDAO = new FacilityJPADAO(DatabaseConfig.getEntityManager());
        System.out.println("H2 database instance initialized successfully.");
    }

    @Test
    public void testAddAndListFacility() {
        System.out.println("Starting test: testAddAndListFacility");

        // Step 1: Create and add a Facility
        System.out.println("Creating a new Facility...");
        Facility facility = new Facility("Tennis Court", new BigDecimal(5), new BigDecimal(25), new BigDecimal(10000),
                new BigDecimal(200));
        System.out.println("Adding the Facility to the database...");
        facilityDAO.add(facility);
        System.out.println("Facility added successfully.");

        // Step 2: Fetch all Facilities
        System.out.println("Fetching all Facilities from the database...");
        List<Facility> facilities = facilityDAO.listAll();
        System.out.println("Facilities fetched successfully. Total facilities retrieved: " + facilities.size());

        // Debugging: Print all retrieved facilities
        for (Facility f : facilities) {
            System.out.println("Retrieved Facility: " + f);
        }

        // Step 3: Validate the results
        System.out.println("Validating retrieved Facilities...");
        assertEquals(1, facilities.size(), "The number of facilities retrieved is incorrect");
        assertEquals("Tennis Court", facilities.get(0).getName(), "The retrieved facility's name is incorrect");
        System.out.println("Validation successful.");

        System.out.println("Test testAddAndListFacility completed successfully.");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the H2 database instance for Facility tests...");
        DatabaseConfig.closeEntityManagerFactory();
        System.out.println("H2 database instance closed successfully.");
    }
}
