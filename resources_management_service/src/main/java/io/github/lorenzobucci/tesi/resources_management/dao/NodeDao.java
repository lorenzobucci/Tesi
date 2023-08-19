package io.github.lorenzobucci.tesi.resources_management.dao;

import io.github.lorenzobucci.tesi.resources_management.model.Node;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class NodeDao {

    @PersistenceContext
    private EntityManager em;

    public void create(Node node) {
        em.persist(node);
    }

    public void delete(Node node) {
        em.remove(em.contains(node) ? node : em.merge(node));
    }

    public void update(Node node) {
        em.merge(node);
    }

    public List<Node> findAll() {
        TypedQuery<Node> query = em.createQuery("select p from Node p", Node.class);
        return query.getResultList();
    }

    public Node findById(long nodeId) {
        return em.find(Node.class, nodeId);
    }

    public Node findByUuid(UUID nodeUuid) {
        TypedQuery<Node> query =
                em.createQuery("select p from Node p where p.uuid = :nodeUuid", Node.class)
                        .setParameter("nodeUuid", nodeUuid);
        return query.getSingleResult();
    }
}
