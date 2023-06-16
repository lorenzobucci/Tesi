package resourcesmanagement;

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

    Set<ContainerInstance> ownedContainer;
    InetAddress ipAddress = null;

    float memoryUsagePercentage;
    float cpuUsagePercentage;

    public Node(NodeType nodeType, NodeTechnicalProperties properties, float latitude, float longitude) {
        id = UUID.randomUUID();
        this.nodeType = nodeType;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
        ownedContainer = new HashSet<>();
    }

    public Node(Node node) {
        id = node.id;
        nodeType = node.nodeType;
        properties = node.properties;
        latitude = node.latitude;
        longitude = node.longitude;
        memoryUsagePercentage = node.memoryUsagePercentage;
        cpuUsagePercentage = node.cpuUsagePercentage;
        ownedContainer = new HashSet<>(node.ownedContainer);
        if (node.ipAddress != null)
            setIpAddress(node.ipAddress);
    }

    public void cleanInactiveContainer() {
        ownedContainer.removeIf(containerInstance -> containerInstance.getContainerState().equals("TERMINATED"));   // or anything else
    }

    void syncWithRealObject(InetAddress ipAddress, float memoryUsagePercentage, float cpuUsagePercentage) {
        setIpAddress(ipAddress);
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.cpuUsagePercentage = cpuUsagePercentage;
    }

    private void setIpAddress(InetAddress ipAddress) {
        try {
            this.ipAddress = InetAddress.getByAddress(ipAddress.getAddress());
        } catch (UnknownHostException ignored) {
        }

        for (ContainerInstance containerInstance : ownedContainer)
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
