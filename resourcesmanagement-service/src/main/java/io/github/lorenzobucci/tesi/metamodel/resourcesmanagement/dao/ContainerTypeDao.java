package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

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
        TypedQuery<ContainerType> query = em.createQuery("from container_type ", ContainerType.class);
        return query.getResultList();
    }

    public ContainerType findById(UUID containerTypeId) {
        return em.find(ContainerType.class, containerTypeId);
    }
}
