package fr.epita.tests;

import fr.epita.config.AppConfig;
import fr.epita.services.DatabaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class TestSPR2 {

    private DataSource dataSource;
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void setup() throws Exception {
        System.out.println("Initializing database and executing base.sql...");

        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the beans from Spring context
        dataSource = context.getBean(DataSource.class);
        entityManagerFactory = context.getBean(EntityManagerFactory.class);

        // Drop and recreate the schema
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS bookings CASCADE;");
            statement.execute("DROP TABLE IF EXISTS facilities CASCADE;");
            statement.execute("DROP TABLE IF EXISTS members CASCADE;");

            // Load SQL from base.sql
            try (BufferedReader reader = new BufferedReader(
                    new FileReader("../core-module/src/main/resources/base.sql"))) {
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
    }

    @Test
    public void testMemberCountUsingJPA() {
        System.out.println("Starting JPA-based test...");

        // Get an EntityManager instance from the EntityManagerFactory
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Use JPA to query the number of members
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(m) FROM Member m", Long.class);
        Long memberCount = query.getSingleResult();
        System.out.println("Member count using JPA: " + memberCount);

        // Assert the member count is as expected
        Assertions.assertEquals(30, memberCount, "The member count in the database is incorrect.");
    }

    @Test
    public void testMemberCountUsingDataSource() throws Exception {
        System.out.println("Starting DataSource-based test...");

        // Query the database using JDBC to count the members
        int memberCount;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             var resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM members")) {

            resultSet.next();
            memberCount = resultSet.getInt("total");
        }

        System.out.println("Member count using DataSource: " + memberCount);

        // Assert the member count is as expected
        Assertions.assertEquals(30, memberCount, "The member count in the database is incorrect.");
    }
}
