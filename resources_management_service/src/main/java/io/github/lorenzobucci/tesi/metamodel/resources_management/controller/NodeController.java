package io.github.lorenzobucci.tesi.metamodel.resources_management.controller;

import io.github.lorenzobucci.tesi.metamodel.resources_management.dao.NodeDao;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.NodeTechnicalProperties;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;

import java.net.InetAddress;
import java.util.List;
import java.util.NoSuchElementException;

public class NodeController {

    @Inject
    private NodeDao nodeDao;

    @Inject
    private ContainerInstanceController containerInstanceController;

    public Node addNode(Node.NodeType nodeType, InetAddress ipAddress, NodeTechnicalProperties properties, float latitude, float longitude) {
        Node node = new Node(nodeType, ipAddress, properties, latitude, longitude);
        nodeDao.create(node);
        return nodeDao.findByUuid(node.getUuid());
    }

    public Node getNode(long nodeId) throws NoSuchElementException {
        Node node = nodeDao.findById(nodeId);
        if (node != null)
            return node;
        else
            throw new NoSuchElementException("Node with id=" + nodeId + " does not exist.");
    }

    public List<Node> retrieveNodes() {
        return nodeDao.findAll();
    }

    public void removeNode(long nodeId) throws NoSuchElementException, ConstraintViolationException {
        nodeDao.delete(getNode(nodeId));
    }

    public void syncNodeDTProperties(long nodeId, float memoryUsagePercentage, float cpuUsagePercentage) throws NoSuchElementException {
        Node node = getNode(nodeId);
        node.syncWithRealObject(memoryUsagePercentage, cpuUsagePercentage);
        nodeDao.update(node);
    }

    void updateNode(Node node) {
        nodeDao.update(node);
    }

}
