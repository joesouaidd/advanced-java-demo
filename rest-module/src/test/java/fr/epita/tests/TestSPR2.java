package fr.epita.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.epita.config.AppConfig;
import fr.epita.services.DatabaseConfig;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSPR2 {

    @Test
    public void testMemberCount() throws Exception {
        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the DataSource bean
        DataSource dataSource = context.getBean(DataSource.class);

        // Query the database to count the members
        int memberCount;
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM members")) {

            resultSet.next();
            memberCount = resultSet.getInt("total");
        }

        // Display the count of members
        System.out.println("Member count in database: " + memberCount);

        // Assert that the member count is as expected (30 based on base.sql)
        Assertions.assertEquals(30, memberCount, "The member count in the database is incorrect.");
    }
}
