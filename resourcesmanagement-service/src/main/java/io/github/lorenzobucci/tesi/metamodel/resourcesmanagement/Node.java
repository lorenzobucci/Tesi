package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Node {
    private final UUID id;

    public enum NodeType {
        CLOUD,
        EDGE
    }

    private final NodeType nodeType;
    private final NodeTechnicalProperties properties;
    private final InetAddress ipAddress;
    private final float latitude;
    private final float longitude;

    private float memoryUsagePercentage;
    private float cpuUsagePercentage;

    private final Set<ContainerInstance> ownedContainers;

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
        Set<ContainerInstance> oldOwnedContainersCopy = new HashSet<>(ownedContainers);
        this.ownedContainers.add(newContainer);
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, ownedContainers);
    }

    void removeOwnedContainer(ContainerInstance ownedContainer) {
        Set<ContainerInstance> oldOwnedContainersCopy = new HashSet<>(ownedContainers);
        this.ownedContainers.remove(ownedContainer);
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, ownedContainers);
    }

    public UUID getId() {
        return id;
    }

    public NodeTechnicalProperties getProperties() {
        return properties;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public float getMemoryUsagePercentage() {
        return memoryUsagePercentage;
    }

    public float getCpuUsagePercentage() {
        return cpuUsagePercentage;
    }

    public int getOwnedContainersSize() {
        return ownedContainers.size();
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
