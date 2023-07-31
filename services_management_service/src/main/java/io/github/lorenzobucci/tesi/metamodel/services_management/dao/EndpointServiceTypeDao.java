package io.github.lorenzobucci.tesi.metamodel.services_management.dao;

import io.github.lorenzobucci.tesi.metamodel.services_management.model.EndpointServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class EndpointServiceTypeDao {

    @PersistenceContext
    private EntityManager em;

    public void create(EndpointServiceType endpointServiceType) {
        em.persist(endpointServiceType);
    }

    public void delete(EndpointServiceType endpointServiceType) {
        em.remove(endpointServiceType);
    }

    public List<EndpointServiceType> findAll() {
        TypedQuery<EndpointServiceType> query = em.createQuery("select p from EndpointServiceType p", EndpointServiceType.class);
        return query.getResultList();
    }

    public EndpointServiceType findById(long endpointServiceTypeId) {
        return em.find(EndpointServiceType.class, endpointServiceTypeId);
    }

    public EndpointServiceType findByUuid(UUID endpointServiceTypeUuid) {
        TypedQuery<EndpointServiceType> query =
                em.createQuery("select p from EndpointServiceType p where p.uuid = :endpointServiceTypeUuid", EndpointServiceType.class)
                        .setParameter("endpointServiceTypeUuid", endpointServiceTypeUuid);
        return query.getSingleResult();
    }

    public EndpointServiceType findByURI(String endpointURI) {
        TypedQuery<EndpointServiceType> query =
                em.createQuery("select p from EndpointServiceType p where p.physicalEndpointURI = :endpointURI", EndpointServiceType.class)
                        .setParameter("endpointURI", endpointURI);
        return query.getSingleResult();
    }
}
