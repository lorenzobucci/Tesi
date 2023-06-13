package resourcesmanagement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.UUID;

public class Node {
    public final UUID id;

    public final NodeType nodeType;

    public enum NodeType {
        CLOUD,
        EDGE
    }

    public final NodeTechnicalProperties properties;

    public final float latitude;
    public final float longitude;

    InetAddress ipAddress = null;

    float memoryUsagePercentage;
    float cpuUsagePercentage;

    public Node(NodeType nodeType, NodeTechnicalProperties properties, float latitude, float longitude) {
        id = UUID.randomUUID();
        this.nodeType = nodeType;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Node(Node node) {
        id = node.id;
        nodeType = node.nodeType;
        properties = node.properties;
        latitude = node.latitude;
        longitude = node.longitude;
        memoryUsagePercentage = node.memoryUsagePercentage;
        cpuUsagePercentage = node.cpuUsagePercentage;
        if (node.ipAddress != null) {
            try {
                ipAddress = InetAddress.getByAddress(node.ipAddress.getAddress());
            } catch (UnknownHostException ignored) {
            }
        }
    }

    public void syncWithRealObject(InetAddress ipAddress, float memoryUsagePercentage, float cpuUsagePercentage) {
        try {
            this.ipAddress = InetAddress.getByAddress(ipAddress.getAddress());
        } catch (UnknownHostException ignored) {
        }
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.cpuUsagePercentage = cpuUsagePercentage;
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
