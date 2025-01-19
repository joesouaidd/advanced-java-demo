package fr.epita.services.data;

import fr.epita.datamodels.Member;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MemberJPADAO {

    private final EntityManager em;

    public MemberJPADAO(EntityManager em) {
        this.em = em;
    }

    public void add(Member member) {
        em.getTransaction().begin(); // Start transaction
        em.persist(member); // Persist the entity
        em.getTransaction().commit(); // Commit transaction
    }

    public List<Member> listAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }
}
