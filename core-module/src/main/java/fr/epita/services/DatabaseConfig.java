package fr.epita.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfig {

    private static EntityManagerFactory emf;

    public static void initializeEntityManagerFactory(String persistenceUnitName) {
        if (emf == null) {
            try {
                System.out.println("Initializing EntityManagerFactory with persistence unit: " + persistenceUnitName);

                emf = Persistence.createEntityManagerFactory(persistenceUnitName);
                System.out.println("EntityManagerFactory initialized successfully.");
            } catch (Exception e) {
                System.err.println("Error initializing EntityManagerFactory: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException(
                    "EntityManagerFactory is not initialized. Call initializeEntityManagerFactory first.");
        }
        return emf.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
}
