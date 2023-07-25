package io.github.lorenzobucci.tesi.metamodel.resources_management.dao;

import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;
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
        TypedQuery<Node> query = em.createQuery("from Node ", Node.class);
        return query.getResultList();
    }

    public Node findById(long nodeId) {
        return em.find(Node.class, nodeId);
    }

    public Node findByUuid(UUID nodeUuid) {
        TypedQuery<Node> query =
                em.createQuery("from Node where uuid = :nodeUuid", Node.class)
                        .setParameter("nodeUuid", nodeUuid);
        return query.getSingleResult();
    }
}
