package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServiceInstanceDao {

    @PersistenceContext
    private EntityManager em;

    public List<WorkflowInstance> findAll() {
        TypedQuery<WorkflowInstance> query = em.createQuery("from WorkflowInstance ", WorkflowInstance.class);
        return query.getResultList();
    }

    public WorkflowInstance findById(long workflowInstanceId) {
        return em.find(WorkflowInstance.class, workflowInstanceId);
    }

}
