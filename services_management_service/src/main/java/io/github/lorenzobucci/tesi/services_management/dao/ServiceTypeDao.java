package io.github.lorenzobucci.tesi.services_management.dao;

import io.github.lorenzobucci.tesi.services_management.model.ServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class ServiceTypeDao {

    @PersistenceContext
    private EntityManager em;

    public void create(ServiceType serviceType) {
        em.persist(serviceType);
    }

    public void delete(ServiceType serviceType) {
        em.remove(em.contains(serviceType) ? serviceType : em.merge(serviceType));
    }

    public List<ServiceType> findAll() {
        TypedQuery<ServiceType> query = em.createQuery("select p from ServiceType p", ServiceType.class);
        return query.getResultList();
    }

    public ServiceType findById(long serviceTypeId) {
        return em.find(ServiceType.class, serviceTypeId);
    }

    public ServiceType findByUuid(UUID serviceTypeUuid) {
        TypedQuery<ServiceType> query =
                em.createQuery("select p from ServiceType p where p.uuid = :serviceTypeUuid", ServiceType.class)
                        .setParameter("serviceTypeUuid", serviceTypeUuid);
        return query.getSingleResult();
    }
}
