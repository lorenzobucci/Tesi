package resourcesmanagement;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    InetAddress ipAddress = null;

    float memoryUsagePercentage;
    float cpuUsagePercentage;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    public Node(NodeType nodeType, NodeTechnicalProperties properties, float latitude, float longitude) {
        id = UUID.randomUUID();
        this.nodeType = nodeType;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
        ownedContainers = new HashSet<>();
    }

    public Node(Node node) {
        id = node.id;
        nodeType = node.nodeType;
        properties = node.properties;
        latitude = node.latitude;
        longitude = node.longitude;
        memoryUsagePercentage = node.memoryUsagePercentage;
        cpuUsagePercentage = node.cpuUsagePercentage;
        ownedContainers = new HashSet<>(node.ownedContainers);
        if (node.ipAddress != null)
            setIpAddress(node.ipAddress);
    }

    public void cleanInactiveContainer() {
        Set<ContainerInstance> oldOwnedContainersCopy = getOwnedContainers();
        ownedContainers.removeIf(containerInstance -> containerInstance.getContainerState().equals("TERMINATED"));   // or anything else
        eventSupport.firePropertyChange("ownedContainers", oldOwnedContainersCopy, getOwnedContainers());
    }

    void addPropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.addPropertyChangeListener(pcl);
    }

    void removePropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.removePropertyChangeListener(pcl);
    }

    void syncWithRealObject(InetAddress ipAddress, float memoryUsagePercentage, float cpuUsagePercentage) {
        setIpAddress(ipAddress);
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

    private void setIpAddress(InetAddress ipAddress) {
        try {
            this.ipAddress = InetAddress.getByAddress(ipAddress.getAddress());
        } catch (UnknownHostException ignored) {
        }

        for (ContainerInstance containerInstance : ownedContainers)
            containerInstance.serviceInstance.nodeIpAddress = this.ipAddress;
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
