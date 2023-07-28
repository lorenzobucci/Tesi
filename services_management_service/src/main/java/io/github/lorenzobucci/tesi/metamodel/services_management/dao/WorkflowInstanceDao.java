package io.github.lorenzobucci.tesi.metamodel.services_management.dao;

import io.github.lorenzobucci.tesi.metamodel.services_management.model.WorkflowInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class WorkflowInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public void create(WorkflowInstance workflowInstance) {
        em.persist(workflowInstance);
    }

    public void update(WorkflowInstance workflowInstance) {
        em.merge(workflowInstance);
    }

    public void delete(WorkflowInstance workflowInstance) {
        em.remove(workflowInstance);
    }

    public List<WorkflowInstance> findAll() {
        TypedQuery<WorkflowInstance> query = em.createQuery("select p from WorkflowInstance p", WorkflowInstance.class);
        return query.getResultList();
    }

    public WorkflowInstance findById(long workflowInstanceId) {
        return em.find(WorkflowInstance.class, workflowInstanceId);
    }

    public WorkflowInstance findByUuid(UUID workflowInstanceUuid) {
        TypedQuery<WorkflowInstance> query =
                em.createQuery("select p from WorkflowInstance p where p.uuid = :workflowUuid", WorkflowInstance.class)
                        .setParameter("workflowUuid", workflowInstanceUuid);
        return query.getSingleResult();
    }
}
