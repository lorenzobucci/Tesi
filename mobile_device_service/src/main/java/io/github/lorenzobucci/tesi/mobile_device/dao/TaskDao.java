package io.github.lorenzobucci.tesi.mobile_device.dao;

import io.github.lorenzobucci.tesi.mobile_device.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class TaskDao {

    @PersistenceContext
    private EntityManager em;

    public List<Task> findAll() {
        TypedQuery<Task> query = em.createQuery("select p from Task p", Task.class);
        return query.getResultList();
    }

    public Task findById(long taskId) {
        return em.find(Task.class, taskId);
    }

}
