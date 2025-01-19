package fr.epita.tests;

import fr.epita.datamodels.Member;
import fr.epita.services.DatabaseConfig;
import fr.epita.services.data.MemberJPADAO;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.List;

public class TestMemberJPADAO {

    private MemberJPADAO memberDAO;

    @BeforeEach
    public void setUp() {
        System.out.println("Initializing H2 database instance for the test...");
        DatabaseConfig.initializeEntityManagerFactory("test-persistence-unit");
        memberDAO = new MemberJPADAO(DatabaseConfig.getEntityManager());
        System.out.println("H2 database instance initialized successfully.");
    }

    @Test
    public void testAddAndListMember() {
        System.out.println("Starting test: testAddAndListMember");

        System.out.println("Step 1: Create and add a Member");
        Member member = new Member("Smith", "John", "123 Main Street", 12345, "123-456-7890", null,
                Timestamp.valueOf("2022-01-01 10:00:00"));
        memberDAO.add(member);
        System.out.println("Member added successfully.");

        System.out.println("Step 2: Retrieve all members and validate");
        List<Member> members = memberDAO.listAll();
        System.out.println("Members retrieved: " + members.size());

        assertEquals(1, members.size(), "The number of members retrieved is incorrect.");
    }
}
