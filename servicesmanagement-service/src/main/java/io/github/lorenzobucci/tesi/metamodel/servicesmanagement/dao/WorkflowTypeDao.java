package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.EndpointServiceType;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class WorkflowTypeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(WorkflowType workflowType) {
        em.persist(workflowType);
    }

    @Transactional
    public void update(WorkflowType workflowType) {
        em.merge(workflowType);
    }

    @Transactional
    public void delete(WorkflowType workflowType) {
        em.remove(workflowType);
    }

    public List<WorkflowType> findAll() {
        TypedQuery<WorkflowType> query = em.createQuery("from WorkflowType ", WorkflowType.class);
        return query.getResultList();
    }

    public WorkflowType findById(long workflowTypeId) {
        return em.find(WorkflowType.class, workflowTypeId);
    }

    public WorkflowType findByUuid(UUID workflowTypeUuid) {
        TypedQuery<WorkflowType> query =
                em.createQuery("from WorkflowType where uuid = :workflowUuid", WorkflowType.class)
                        .setParameter("workflowUuid", workflowTypeUuid);
        return query.getSingleResult();
    }

    public WorkflowType findByEndpoint(EndpointServiceType endpointServiceType) {
        TypedQuery<WorkflowType> query =
                em.createQuery("from WorkflowType where endpointServiceType = :endpointId", WorkflowType.class)
                        .setParameter("endpointId", endpointServiceType.getId());
        return query.getSingleResult();
    }
}
