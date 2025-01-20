package fr.epita.tests;

import fr.epita.datamodels.Member;
import fr.epita.services.data.MemberJPADAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import fr.epita.config.AppConfig;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMemberJPADAO {

    private ApplicationContext context;
    private MemberJPADAO memberDAO;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Initializing Spring Application Context and cleaning database...");

        // Initialize Spring context
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve DAOs from Spring context
        memberDAO = context.getBean(MemberJPADAO.class);

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
    public void testAddAndListMember() {
        System.out.println("Starting test: testAddAndListMember");

        // Step 1: Create and add a Member
        Member member = new Member("Doe", "Jane", "456 Elm Street", 54321, "987-654-3210", null,
                Timestamp.valueOf("2023-01-01 09:00:00"));
        memberDAO.add(member);

        // Step 2: Fetch all Members
        List<Member> members = memberDAO.listAll();

        // Step 3: Validate the results
        assertEquals(1, members.size(), "The number of members retrieved is incorrect.");
        assertEquals("Jane", members.get(0).getFirstname(), "The retrieved member's first name is incorrect");

        System.out.println("Test testAddAndListMember completed successfully.");
    }
}
