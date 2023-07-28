package io.github.lorenzobucci.tesi.metamodel.services_management.dao;

import io.github.lorenzobucci.tesi.metamodel.services_management.model.ServiceInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class ServiceInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public List<ServiceInstance> findAll() {
        TypedQuery<ServiceInstance> query = em.createQuery("select p from ServiceInstance p", ServiceInstance.class);
        return query.getResultList();
    }

    public ServiceInstance findById(long serviceInstanceId) {
        return em.find(ServiceInstance.class, serviceInstanceId);
    }

}
