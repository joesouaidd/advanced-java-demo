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
import java.util.Properties;

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

        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Create H2 DataSource
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        // Load SQL files
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            loadSQLFile(statement, "./src/main/resources/create-members.sql");
            loadSQLFile(statement, "./src/main/resources/create-facilities.sql");
            loadSQLFile(statement, "./src/main/resources/create-bookings.sql");
            loadSQLFile(statement, "./src/main/resources/insert-members.sql");
            loadSQLFile(statement, "./src/main/resources/insert-facilities.sql");
            loadSQLFile(statement, "./src/main/resources/insert-bookings.sql");

            connection.commit();

            System.out.println("SQL data loaded successfully.");
        }

        return dataSource;
    }

    private void loadSQLFile(Statement statement, String filePath) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }

            String sql = sqlBuilder.toString();
            statement.execute(sql);
            System.out.println("Loaded SQL from: " + filePath);
        }
    }

    // Define a bean for EntityManagerFactory
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        System.out.println("Initializing EntityManagerFactory...");
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "none"); // Do not recreate schema
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");

        return Persistence.createEntityManagerFactory("test-persistence-unit", properties);
    }

    // @Bean
    // public EntityManagerFactory entityManagerFactory() {
    // System.out.println("Initializing EntityManagerFactory...");
    // return Persistence.createEntityManagerFactory("test-persistence-unit");
    // }
}
