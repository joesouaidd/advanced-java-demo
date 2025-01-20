package fr.epita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import fr.epita.services.data.BookingJPADAO;
import fr.epita.services.data.FacilityJPADAO;
import fr.epita.services.data.MemberJPADAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@Configuration
public class AppConfig {

    // Define a DataSource bean for the H2 database and load SQL files
    @Bean
    public DataSource dataSource() throws Exception {
        System.out.println("Initializing H2 DataSource and loading SQL data...");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        // Load the base directory property
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        String baseDir = properties.getProperty("sql.base.dir");

        // Convert the base directory to an absolute path
        String absoluteBaseDir = new java.io.File(baseDir).getCanonicalPath();
        System.out.println("Resolved absolute base directory: " + absoluteBaseDir);

        // Load SQL files to initialize the database
        // if (!isDatabaseInitialized(dataSource)) {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            loadSQLFile(statement, absoluteBaseDir + "/create-members.sql");
            loadSQLFile(statement, absoluteBaseDir + "/create-facilities.sql");
            loadSQLFile(statement, absoluteBaseDir + "/create-bookings.sql");
            loadSQLFile(statement, absoluteBaseDir + "/insert-members.sql");
            loadSQLFile(statement, absoluteBaseDir + "/insert-facilities.sql");
            loadSQLFile(statement, absoluteBaseDir + "/insert-bookings.sql");

            connection.commit();

            System.out.println("SQL data loaded successfully.");
        }
        // } else {
        // System.out.println("Database already initialized. Skipping SQL data load.");
        // }

        return dataSource;
    }

    // private boolean isDatabaseInitialized(DataSource dataSource) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         DatabaseMetaData metaData = connection.getMetaData();
    //         try (ResultSet resultSet = metaData.getTables(null, null, "FACILITIES", null)) {
    //             return resultSet.next(); // If there is a result, the "FACILITIES" table exists
    //         }
    //     } catch (Exception e) {
    //         System.err.println("Error checking database initialization: " + e.getMessage());
    //     }
    //     return false; // Assume not initialized if there is any error
    // }

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

    // Define a LocalContainerEntityManagerFactoryBean for JPA
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("fr.epita.datamodels");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none"); // Schema managed externally
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.format_sql", "true");

        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    // Define a JpaTransactionManager bean

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        EntityManagerFactory emf = entityManagerFactory.getObject();
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory could not be created");
        }
        return new JpaTransactionManager(emf);
    }

    @Bean
    public FacilityJPADAO facilityJPADAO(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return new FacilityJPADAO(entityManager);
    }

    @Bean
    public MemberJPADAO memberJPADAO(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return new MemberJPADAO(entityManager);
    }

    @Bean
    public BookingJPADAO bookingJPADAO(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return new BookingJPADAO(entityManager);
    }

}
