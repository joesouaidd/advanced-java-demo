package fr.epita.tests;

import fr.epita.datamodels.Booking;
import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import fr.epita.services.DatabaseConfig;
import fr.epita.services.data.BookingJPADAO;
import fr.epita.services.data.FacilityJPADAO;
import fr.epita.services.data.MemberJPADAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBookingJPADAO {

    private FacilityJPADAO facilityDAO;
    private MemberJPADAO memberDAO;
    private BookingJPADAO bookingDAO;

    @BeforeEach

    public void setUp() {
        System.out.println("Initializing H2 database instance for Booking tests...");

        // he schema.sql file is executed at this point, creating the necessary tables
        // (facilities, members, bookings) in the H2 database.
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");

        facilityDAO = new FacilityJPADAO(DatabaseConfig.getEntityManager());
        memberDAO = new MemberJPADAO(DatabaseConfig.getEntityManager());
        bookingDAO = new BookingJPADAO(DatabaseConfig.getEntityManager());
        System.out.println("H2 database instance initialized successfully.");
    }

    @Test
    public void testAddAndListBooking() {
        System.out.println("Starting test: testAddAndListBooking");

        // Step 1: Create and add a Facility
        System.out.println("Creating a new Facility...");
        Facility facility = new Facility("Tennis Court", new BigDecimal(5), new BigDecimal(25), new BigDecimal(10000),
                new BigDecimal(200));
        System.out.println("Adding the Facility to the database...");
        facilityDAO.add(facility);
        System.out.println("Facility added successfully.");

        // Step 2: Create and add a Member
        System.out.println("Creating a new Member...");
        Member member = new Member("Doe", "Jane", "456 Elm Street", 54321, "987-654-3210", null,
                Timestamp.valueOf("2023-01-01 09:00:00"));
        System.out.println("Adding the Member to the database...");
        memberDAO.add(member);
        System.out.println("Member added successfully.");

        // Step 3: Create and add a Booking
        System.out.println("Creating a new Booking...");
        Booking booking = new Booking(facility, member, Timestamp.valueOf("2023-02-01 14:00:00"), 2);
        System.out.println("Adding the Booking to the database...");
        bookingDAO.add(booking);
        System.out.println("Booking added successfully.");

        // Step 4: Fetch all Bookings
        System.out.println("Fetching all Bookings from the database...");
        List<Booking> bookings = bookingDAO.listAll();
        System.out.println("Bookings fetched successfully. Total bookings retrieved: " + bookings.size());

        // Step 5: Validate the results
        System.out.println("Validating retrieved Bookings...");
        assertEquals(1, bookings.size(), "The number of bookings retrieved is incorrect");
        assertEquals(member.getFirstname(), bookings.get(0).getMember().getFirstname(),
                "The retrieved booking's member is incorrect");
        assertEquals(facility.getName(), bookings.get(0).getFacility().getName(),
                "The retrieved booking's facility is incorrect");
        System.out.println("Validation successful.");

        System.out.println("Test testAddAndListBooking completed successfully.");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the H2 database instance for Booking tests...");
        DatabaseConfig.closeEntityManagerFactory();
        System.out.println("H2 database instance closed successfully.");
    }
}
