package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

public class ContainerTypeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(ContainerType containerType) {
        em.persist(containerType);
    }

    @Transactional
    public void delete(ContainerType containerType) {
        em.remove(containerType);
    }

    public List<ContainerType> findAll() {
        TypedQuery<ContainerType> query = em.createQuery("from ContainerType ", ContainerType.class);
        return query.getResultList();
    }

    public ContainerType findById(long containerTypeId) {
        return em.find(ContainerType.class, containerTypeId);
    }
}
