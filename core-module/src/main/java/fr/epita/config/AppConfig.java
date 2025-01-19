package fr.epita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

import org.h2.jdbcx.JdbcDataSource;

@Configuration
public class AppConfig {

    // Define a test string bean
    @Bean
    public String testString() {
        return "test from spring!";
    }

    // Define a DataSource bean for the H2 database
    @Bean
    public DataSource dataSource() throws Exception {
        System.out.println("Initializing H2 DataSource and loading SQL data...");

        // Create H2 DataSource
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        // Load SQL file
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                BufferedReader reader = new BufferedReader(new FileReader(
                        "C:/Users/User/Desktop/Adv-Java-SampleExam/core-module/src/main/resources/base.sql"))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }

            String sql = sqlBuilder.toString();
            statement.execute(sql);
            connection.commit();

            System.out.println("SQL data from base.sql loaded successfully.");
        }

        return dataSource;
    }

    // Define a bean for EntityManagerFactory
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        System.out.println("Initializing EntityManagerFactory...");
        return Persistence.createEntityManagerFactory("test-persistence-unit");
    }
}
