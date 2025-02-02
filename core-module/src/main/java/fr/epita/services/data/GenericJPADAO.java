package fr.epita.services.data;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class GenericJPADAO<T, ID> {

    protected EntityManager em;

    public GenericJPADAO(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void add(T entityInstance) {
        em.persist(entityInstance);
    }

    @Transactional
    public void update(T entityInstance) {
        em.merge(entityInstance);
    }

    @Transactional
    public void delete(T entityInstance) {
        em.remove(entityInstance);
    }

    @Transactional
    public void deleteById(Class<T> objectClass, ID id) {
        em.remove(em.find(objectClass, id));
    }
}
