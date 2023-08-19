package io.github.lorenzobucci.tesi.resources_management.dao;

import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class ContainerInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public void update(ContainerInstance containerInstance) {
        em.merge(containerInstance);
    }

    public List<ContainerInstance> findAll() {
        TypedQuery<ContainerInstance> query = em.createQuery("select p from ContainerInstance p", ContainerInstance.class);
        return query.getResultList();
    }

    public ContainerInstance findById(long containerInstanceId) {
        return em.find(ContainerInstance.class, containerInstanceId);
    }

    public ContainerInstance findByUuid(UUID containerInstanceUuid) {
        TypedQuery<ContainerInstance> query =
                em.createQuery("select p from ContainerInstance p where p.uuid = :containerUuid", ContainerInstance.class)
                        .setParameter("containerUuid", containerInstanceUuid);
        return query.getSingleResult();
    }
}
