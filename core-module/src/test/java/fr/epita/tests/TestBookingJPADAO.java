package fr.epita.tests;

import fr.epita.config.AppConfig;
import fr.epita.datamodels.Booking;
import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import fr.epita.services.data.BookingJPADAO;
import fr.epita.services.data.FacilityJPADAO;
import fr.epita.services.data.MemberJPADAO;
import org.springframework.context.ApplicationContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBookingJPADAO {

    private ApplicationContext context;

    private FacilityJPADAO facilityDAO;
    private MemberJPADAO memberDAO;
    private BookingJPADAO bookingDAO;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Initializing Spring Application Context and cleaning database...");

        // Initialize Spring context
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve DAOs from Spring context
        facilityDAO = context.getBean(FacilityJPADAO.class);
        memberDAO = context.getBean(MemberJPADAO.class);
        bookingDAO = context.getBean(BookingJPADAO.class);

        // Clean up database to ensure tests run in isolation
        try (Connection connection = context.getBean(DataSource.class).getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM bookings");
            statement.execute("DELETE FROM facilities");
            statement.execute("DELETE FROM members");
            System.out.println("Database cleaned successfully.");
        }
    }

    @AfterAll
    public static void tearDown() {

        System.out.println("TestBookingJPADAO completed successfully.");
    }

    @Test
    public void testAddAndListBooking() {
        // Step 1: Create and add a Facility
        Facility facility = new Facility("Tennis Court", new BigDecimal(5), new BigDecimal(25), new BigDecimal(10000),
                new BigDecimal(200));
        facilityDAO.add(facility);

        // Step 2: Create and add a Member
        Member member = new Member("Doe", "Jane", "456 Elm Street", 54321, "987-654-3210", null,
                Timestamp.valueOf("2023-01-01 09:00:00"));
        memberDAO.add(member);

        // Step 3: Create and add a Booking
        Booking booking = new Booking(facility, member, Timestamp.valueOf("2023-02-01 14:00:00"), 2);
        bookingDAO.add(booking);

        // Step 4: Fetch all Bookings
        List<Booking> bookings = bookingDAO.listAll();

        // Step 5: Validate the results
        assertEquals(1, bookings.size(), "The number of bookings retrieved is incorrect");
        assertEquals(member.getFirstname(), bookings.get(0).getMember().getFirstname(),
                "The retrieved booking's member is incorrect");
        assertEquals(facility.getName(), bookings.get(0).getFacility().getName(),
                "The retrieved booking's facility is incorrect");
    }

}
