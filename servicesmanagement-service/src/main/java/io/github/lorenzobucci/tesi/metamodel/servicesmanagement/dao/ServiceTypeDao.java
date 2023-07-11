package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

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
}
