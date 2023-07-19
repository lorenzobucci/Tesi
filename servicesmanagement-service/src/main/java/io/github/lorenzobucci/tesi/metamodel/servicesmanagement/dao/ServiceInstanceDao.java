package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServiceInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public List<ServiceInstance> findAll() {
        TypedQuery<ServiceInstance> query = em.createQuery("from ServiceInstance ", ServiceInstance.class);
        return query.getResultList();
    }

    public ServiceInstance findById(long serviceInstanceId) {
        return em.find(ServiceInstance.class, serviceInstanceId);
    }

}
