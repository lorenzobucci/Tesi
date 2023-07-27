package io.github.lorenzobucci.tesi.metamodel.services_management.dao;

import io.github.lorenzobucci.tesi.metamodel.services_management.model.EndpointServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    public EndpointServiceType findByUuid(UUID endpointServiceTypeUuid) {
        TypedQuery<EndpointServiceType> query =
                em.createQuery("from EndpointServiceType where uuid = :endpointServiceTypeUuid", EndpointServiceType.class)
                        .setParameter("endpointServiceTypeUuid", endpointServiceTypeUuid);
        return query.getSingleResult();
    }

    public EndpointServiceType findByURI(URI endpointURI) {
        TypedQuery<EndpointServiceType> query =
                em.createQuery("from EndpointServiceType where physicalEndpointURI = :endpointURI", EndpointServiceType.class)
                        .setParameter("endpointURI", endpointURI);
        return query.getSingleResult();
    }
}