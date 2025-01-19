package fr.epita.services.data;

import fr.epita.datamodels.Facility;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FacilityJPADAO {

    private final EntityManager em;

    public FacilityJPADAO(EntityManager em) {
        this.em = em;
    }

    public void add(Facility facility) {
        em.getTransaction().begin(); // Start transaction
        em.persist(facility); // Persist the facility
        em.getTransaction().commit(); // Commit transaction
    }

    public List<Facility> listAll() {
        return em.createQuery("SELECT f FROM Facility f", Facility.class).getResultList();
    }
}
