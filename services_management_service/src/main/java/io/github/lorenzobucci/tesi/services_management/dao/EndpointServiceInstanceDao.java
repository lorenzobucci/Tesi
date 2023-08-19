package io.github.lorenzobucci.tesi.services_management.dao;

import io.github.lorenzobucci.tesi.services_management.model.EndpointServiceInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class EndpointServiceInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public List<EndpointServiceInstance> findAll() {
        TypedQuery<EndpointServiceInstance> query = em.createQuery("select p from EndpointServiceInstance p", EndpointServiceInstance.class);
        return query.getResultList();
    }

    public EndpointServiceInstance findById(long endpointServiceInstanceId) {
        return em.find(EndpointServiceInstance.class, endpointServiceInstanceId);
    }

}
