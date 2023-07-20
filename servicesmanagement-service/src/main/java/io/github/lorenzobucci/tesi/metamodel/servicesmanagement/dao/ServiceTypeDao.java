package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class ServiceTypeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(ServiceType serviceType) {
        em.persist(serviceType);
    }

    @Transactional
    public void delete(ServiceType serviceType) {
        em.remove(serviceType);
    }

    public List<ServiceType> findAll() {
        TypedQuery<ServiceType> query = em.createQuery("from ServiceType ", ServiceType.class);
        return query.getResultList();
    }

    public ServiceType findById(long serviceTypeId) {
        return em.find(ServiceType.class, serviceTypeId);
    }

    public ServiceType findByUuid(UUID serviceTypeUuid) {
        TypedQuery<ServiceType> query =
                em.createQuery("from ServiceType where uuid = :serviceTypeUuid", ServiceType.class)
                        .setParameter("serviceTypeUuid", serviceTypeUuid);
        return query.getSingleResult();
    }
}
