package io.github.lorenzobucci.tesi.resources_management.model;

import jakarta.persistence.*;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "node")
public class Node extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, mappedBy = "belongingNode")
    private Set<ContainerInstance> ownedContainers = new HashSet<>();

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

    public enum NodeType {
        CLOUD,
        EDGE
    }

    public Node(NodeType nodeType, InetAddress ipAddress, NodeTechnicalProperties properties, float latitude, float longitude) {
        this.nodeType = nodeType;
        this.ipAddress = ipAddress;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Node() {
    }

    public void syncWithRealObject(float memoryUsagePercentage, float cpuUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.cpuUsagePercentage = cpuUsagePercentage;
    }

    public void addOwnedContainer(ContainerInstance newContainer) {
        this.ownedContainers.add(newContainer);
        newContainer.updateBelongingNode(this);
    }

    public void removeOwnedContainer(ContainerInstance ownedContainer) {
        this.ownedContainers.remove(ownedContainer);
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

    public Set<ContainerInstance> getOwnedContainers() {
        return ownedContainers;
    }

}
