package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.EndpointServiceInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EndpointServiceInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public List<EndpointServiceInstance> findAll() {
        TypedQuery<EndpointServiceInstance> query = em.createQuery("from EndpointServiceInstance ", EndpointServiceInstance.class);
        return query.getResultList();
    }

    public EndpointServiceInstance findById(long endpointServiceInstanceId) {
        return em.find(EndpointServiceInstance.class, endpointServiceInstanceId);
    }

}
