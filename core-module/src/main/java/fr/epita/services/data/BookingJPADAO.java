package fr.epita.services.data;

import fr.epita.datamodels.Booking;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BookingJPADAO {

    private final EntityManager em;

    public BookingJPADAO(EntityManager em) {
        this.em = em;
    }

    public void add(Booking booking) {
        em.getTransaction().begin(); // Start transaction
        em.persist(booking); // Persist the booking entity
        em.getTransaction().commit(); // Commit transaction
    }

    public List<Booking> listAll() {
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }
}
