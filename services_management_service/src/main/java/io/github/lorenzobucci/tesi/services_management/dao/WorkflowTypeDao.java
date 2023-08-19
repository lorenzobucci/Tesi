package io.github.lorenzobucci.tesi.services_management.dao;

import io.github.lorenzobucci.tesi.services_management.model.EndpointServiceType;
import io.github.lorenzobucci.tesi.services_management.model.WorkflowType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class WorkflowTypeDao {

    @PersistenceContext
    private EntityManager em;

    public void create(WorkflowType workflowType) {
        em.persist(workflowType);
    }

    public void update(WorkflowType workflowType) {
        em.merge(workflowType);
    }

    public void delete(WorkflowType workflowType) {
        em.remove(em.contains(workflowType) ? workflowType : em.merge(workflowType));
    }

    public List<WorkflowType> findAll() {
        TypedQuery<WorkflowType> query = em.createQuery("select p from WorkflowType p", WorkflowType.class);
        return query.getResultList();
    }

    public WorkflowType findById(long workflowTypeId) {
        return em.find(WorkflowType.class, workflowTypeId);
    }

    public WorkflowType findByUuid(UUID workflowTypeUuid) {
        TypedQuery<WorkflowType> query =
                em.createQuery("select p from WorkflowType p where p.uuid = :workflowUuid", WorkflowType.class)
                        .setParameter("workflowUuid", workflowTypeUuid);
        return query.getSingleResult();
    }

    public WorkflowType findByEndpoint(EndpointServiceType endpointServiceType) {
        TypedQuery<WorkflowType> query =
                em.createQuery("select p from WorkflowType p where p.endpointServiceType = :endpoint", WorkflowType.class)
                        .setParameter("endpoint", endpointServiceType);
        return query.getSingleResult();
    }
}
