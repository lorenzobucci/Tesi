package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model;

import jakarta.persistence.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "node")
public class Node {

    @Id
    private UUID id = UUID.randomUUID();

    public enum NodeType {
        CLOUD,
        EDGE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false)
    private NodeType nodeType;

    @Embedded
    private NodeTechnicalProperties properties;

    @Column(name = "ip_address", nullable = false)
    private InetAddress ipAddress;
    private float latitude;
    private float longitude;

    @Transient
    private float memoryUsagePercentage;

    @Transient
    private float cpuUsagePercentage;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "node_container_instances",
            joinColumns = {@JoinColumn(name = "node_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "container_instance_id", referencedColumnName = "id")}
    )
    private Set<ContainerInstance> ownedContainers = new HashSet<>();

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    public Node(NodeType nodeType, InetAddress ipAddress, NodeTechnicalProperties properties, float latitude, float longitude) {
        this.nodeType = nodeType;
        this.ipAddress = ipAddress;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Node() {
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.removePropertyChangeListener(pcl);
    }

    public void syncWithRealObject(float memoryUsagePercentage, float cpuUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.cpuUsagePercentage = cpuUsagePercentage;
    }

    public void addOwnedContainer(ContainerInstance newContainer) {
        Set<ContainerInstance> oldOwnedContainersCopy = new HashSet<>(ownedContainers);
        this.ownedContainers.add(newContainer);
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, ownedContainers);
    }

    public void removeOwnedContainer(ContainerInstance ownedContainer) {
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
