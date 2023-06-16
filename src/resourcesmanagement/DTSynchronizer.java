package resourcesmanagement;

import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.UUID;

public class DTSynchronizer {

    private static final AllocationManager manager = AllocationManager.getInstance();

    private DTSynchronizer() {
    }

    public static void subscribeContainerInstanceProperties(UUID containerInstanceId, PropertyChangeListener pcl) {
        if (manager.activeContainerInstances.containsKey(containerInstanceId))
            manager.activeContainerInstances.get(containerInstanceId).addPropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void unsubscribeContainerInstanceProperties(UUID containerInstanceId, PropertyChangeListener pcl) {
        if (manager.activeContainerInstances.containsKey(containerInstanceId))
            manager.activeContainerInstances.get(containerInstanceId).removePropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void syncContainerInstanceProperties(UUID containerInstanceId, String containerState) {
        if (manager.activeContainerInstances.containsKey(containerInstanceId))
            manager.activeContainerInstances.get(containerInstanceId).syncWithRealObject(containerState);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void syncNodeProperties(UUID nodeId, InetAddress ipAddress, float memoryUsagePercentage, float cpuUsagePercentage) {
        if (manager.availableNodes.containsKey(nodeId))
            manager.availableNodes.get(nodeId).syncWithRealObject(ipAddress, memoryUsagePercentage, cpuUsagePercentage);
        else
            throw new NoSuchElementException("The node " + nodeId + " does not exist.");
    }
}
