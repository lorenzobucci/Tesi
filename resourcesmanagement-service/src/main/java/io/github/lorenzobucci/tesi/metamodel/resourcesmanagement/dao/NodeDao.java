package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.dao;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.Node;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class NodeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(Node node) {
        em.persist(node);
    }

    @Transactional
    public void delete(Node node) {
        em.remove(node);
    }

    @Transactional
    public void update(Node node) {
        em.merge(node);
    }

    public List<Node> findAll() {
        TypedQuery<Node> query = em.createQuery("from node ", Node.class);
        return query.getResultList();
    }

    public Node findById(UUID nodeId) {
        return em.find(Node.class, nodeId);
    }
}
