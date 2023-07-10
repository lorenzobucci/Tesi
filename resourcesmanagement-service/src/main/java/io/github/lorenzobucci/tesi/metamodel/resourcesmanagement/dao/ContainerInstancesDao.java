package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class ContainerInstancesDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(ContainerInstance containerInstance) {
        em.persist(containerInstance);
    }

    @Transactional
    public void delete(ContainerInstance containerInstance) {
        em.remove(containerInstance);
    }

    @Transactional
    public void update(ContainerInstance containerInstance) {
        em.merge(containerInstance);
    }

    public List<ContainerInstance> findAll() {
        TypedQuery<ContainerInstance> query = em.createQuery("from container_instance ", ContainerInstance.class);
        return query.getResultList();
    }

    public ContainerInstance findById(UUID containerInstanceId) {
        return em.find(ContainerInstance.class, containerInstanceId);
    }
}
