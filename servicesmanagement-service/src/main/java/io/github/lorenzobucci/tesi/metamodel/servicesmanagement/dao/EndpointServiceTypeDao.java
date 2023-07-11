package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.EndpointServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

public class EndpointServiceTypeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(EndpointServiceType endpointServiceType) {
        em.persist(endpointServiceType);
    }

    @Transactional
    public void delete(EndpointServiceType endpointServiceType) {
        em.remove(endpointServiceType);
    }

    public List<EndpointServiceType> findAll() {
        TypedQuery<EndpointServiceType> query = em.createQuery("from EndpointServiceType ", EndpointServiceType.class);
        return query.getResultList();
    }

    public EndpointServiceType findById(long endpointServiceTypeId) {
        return em.find(EndpointServiceType.class, endpointServiceTypeId);
    }
}
