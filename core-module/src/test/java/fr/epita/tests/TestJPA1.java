package fr.epita.tests;

import fr.epita.config.AppConfig;
import fr.epita.datamodels.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;

public class TestJPA1 {

    @Test
    public void testPersistMember() {
        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the EntityManagerFactory bean
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);

        // Create an EntityManager
        EntityManager em = emf.createEntityManager();

        // Begin transaction
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // Create a new Member instance
        Member member = new Member();
        member.setSurname("Doe");
        member.setFirstname("John");
        member.setAddress("123 Example Street");
        member.setZipcode(12345);
        member.setTelephone("123-456-7890");
        member.setJoinDate(Timestamp.valueOf("2023-01-01 09:00:00"));

        // Persist the member
        em.persist(member);

        // Commit transaction
        transaction.commit();

        // Verify that the member was persisted
        Member retrievedMember = em.find(Member.class, member.getMemid());
        Assertions.assertNotNull(retrievedMember, "Member should be persisted in the database");
        Assertions.assertEquals("Doe", retrievedMember.getSurname(), "The member's surname should match");

        // Clean up
        em.close();
        emf.close();

        System.out.println("TestJPA1 completed successfully.");
    }
}
