package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class WorkflowInstanceDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(WorkflowInstance workflowInstance) {
        em.persist(workflowInstance);
    }

    @Transactional
    public void update(WorkflowInstance workflowInstance) {
        em.merge(workflowInstance);
    }

    @Transactional
    public void delete(WorkflowInstance workflowInstance) {
        em.remove(workflowInstance);
    }

    public List<WorkflowInstance> findAll() {
        TypedQuery<WorkflowInstance> query = em.createQuery("from WorkflowInstance ", WorkflowInstance.class);
        return query.getResultList();
    }

    public WorkflowInstance findById(long workflowInstanceId) {
        return em.find(WorkflowInstance.class, workflowInstanceId);
    }

    public WorkflowInstance findByUuid(UUID workflowInstanceUuid) {
        TypedQuery<WorkflowInstance> query =
                em.createQuery("from WorkflowInstance where uuid = :workflowUuid", WorkflowInstance.class)
                        .setParameter("workflowUuid", workflowInstanceUuid);
        return query.getSingleResult();
    }
}
