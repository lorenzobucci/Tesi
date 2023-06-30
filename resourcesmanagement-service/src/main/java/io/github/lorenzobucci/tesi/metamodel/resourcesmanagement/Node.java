package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Node {
    public final UUID id;

    public final NodeTechnicalProperties properties;
    public final NodeType nodeType;

    public enum NodeType {
        CLOUD,
        EDGE
    }

    public final float latitude;
    public final float longitude;

    private final Set<ContainerInstance> ownedContainers;
    final InetAddress ipAddress;

    float memoryUsagePercentage;
    float cpuUsagePercentage;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    public Node(NodeType nodeType, InetAddress ipAddress, NodeTechnicalProperties properties, float latitude, float longitude) {
        id = UUID.randomUUID();
        this.nodeType = nodeType;
        this.ipAddress = ipAddress;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
        ownedContainers = new HashSet<>();
    }

    public Node(Node node) {
        id = node.id;
        nodeType = node.nodeType;
        ipAddress = node.ipAddress;
        properties = node.properties;
        latitude = node.latitude;
        longitude = node.longitude;
        memoryUsagePercentage = node.memoryUsagePercentage;
        cpuUsagePercentage = node.cpuUsagePercentage;
        ownedContainers = new HashSet<>(node.ownedContainers);
    }

    void addPropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.addPropertyChangeListener(pcl);
    }

    void removePropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.removePropertyChangeListener(pcl);
    }

    void syncWithRealObject(float memoryUsagePercentage, float cpuUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.cpuUsagePercentage = cpuUsagePercentage;
    }

    void addOwnedContainer(ContainerInstance newContainer) {
        Set<ContainerInstance> oldOwnedContainersCopy = getOwnedContainers();
        this.ownedContainers.add(newContainer);
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, getOwnedContainers());
    }

    void removeOwnedContainer(ContainerInstance ownedContainer) {
        Set<ContainerInstance> oldOwnedContainersCopy = getOwnedContainers();
        this.ownedContainers.remove(ownedContainer);
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, getOwnedContainers());
    }

    public Set<ContainerInstance> getOwnedContainers() {
        return new HashSet<>(ownedContainers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
