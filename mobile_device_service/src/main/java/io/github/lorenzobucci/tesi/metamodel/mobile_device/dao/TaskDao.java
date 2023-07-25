package io.github.lorenzobucci.tesi.metamodel.mobile_device.dao;

import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TaskDao {

    @PersistenceContext
    private EntityManager em;

    public List<Task> findAll() {
        TypedQuery<Task> query = em.createQuery("from Task ", Task.class);
        return query.getResultList();
    }

    public Task findById(long taskId) {
        return em.find(Task.class, taskId);
    }

}
