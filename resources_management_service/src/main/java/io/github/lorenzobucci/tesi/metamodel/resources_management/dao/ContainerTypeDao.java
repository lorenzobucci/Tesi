package io.github.lorenzobucci.tesi.metamodel.resources_management.dao;

import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class ContainerTypeDao {

    @PersistenceContext
    private EntityManager em;

    public void create(ContainerType containerType) {
        em.persist(containerType);
    }

    public void delete(ContainerType containerType) {
        em.remove(containerType);
    }

    public List<ContainerType> findAll() {
        TypedQuery<ContainerType> query = em.createQuery("select p from ContainerType p", ContainerType.class);
        return query.getResultList();
    }

    public ContainerType findById(long containerTypeId) {
        return em.find(ContainerType.class, containerTypeId);
    }

    public ContainerType findByUuid(UUID containerTypeUuid) {
        TypedQuery<ContainerType> query =
                em.createQuery("select p from ContainerType p where p.uuid = :containerUuid", ContainerType.class)
                        .setParameter("containerUuid", containerTypeUuid);
        return query.getSingleResult();
    }
}
