package resourcesmanagement;

import java.net.InetAddress;
import java.util.UUID;

public class DTSynchronizer {

    private static final AllocationManager manager = AllocationManager.getInstance();

    private DTSynchronizer() {
    }

    public static void syncContainerInstance(UUID containerInstanceId, String containerState) {
        if (manager.activeContainerInstances.containsKey(containerInstanceId))
            manager.activeContainerInstances.get(containerInstanceId).syncWithRealObject(containerState);
        else
            throw new IllegalArgumentException("The container " + containerInstanceId + " does not exist");
    }

    public static void syncNode(UUID nodeId, InetAddress ipAddress, float memoryUsagePercentage, float cpuUsagePercentage) {
        if (manager.availableNodes.containsKey(nodeId))
            manager.availableNodes.get(nodeId).syncWithRealObject(ipAddress, memoryUsagePercentage, cpuUsagePercentage);
        else
            throw new IllegalArgumentException("The node " + nodeId + " does not exist");
    }
}
