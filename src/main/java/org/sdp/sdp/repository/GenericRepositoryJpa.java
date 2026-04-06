package org.sdp.sdp.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import util.JPAUtil;

public class GenericRepositoryJpa<T> implements GenericRepository<T> {
    protected final EntityManager em;
    private final Class<T> type;

    public GenericRepositoryJpa(Class<T> type) {
        this.type = type;
        this.em = JPAUtil.getENTITY_MANAGER_FACTORY().createEntityManager();
    }

    public void closePersistency() {
        em.close();
    }

    public void startTransaction() {
        em.getTransaction().begin();
    }

    public void commitTransaction() {
        em.getTransaction().commit();
    }

    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    public List<T> findAll() {
        var query = "select entity from " + type.getName() + " entity";
        return em.createQuery(query, type).getResultList();
    }

    public <U> T get(U id) {
        return em.find(type, id);
    }

    public T update(T object) {
        return em.merge(object);
    }

    public void delete(T object) {
        em.remove(em.merge(object));
    }

    public void insert(T object) {
        em.persist(object);
    }

    public <U> boolean exists(U id) {
        return em.find(type, id) != null;
    }
}