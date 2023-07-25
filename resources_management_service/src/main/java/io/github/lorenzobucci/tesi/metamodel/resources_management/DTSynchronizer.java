package io.github.lorenzobucci.tesi.metamodel.resources_management;

import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import java.util.UUID;

public class DTSynchronizer {

    private static final AllocationManager manager = AllocationManager.getInstance();

    private DTSynchronizer() {
    }

    public static void subscribeContainerInstanceProperties(UUID containerInstanceId, PropertyChangeListener pcl) {
        if (manager.getActiveContainerInstances().containsKey(containerInstanceId))
            manager.getActiveContainerInstances().get(containerInstanceId).addPropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void unsubscribeContainerInstanceProperties(UUID containerInstanceId, PropertyChangeListener pcl) {
        if (manager.getActiveContainerInstances().containsKey(containerInstanceId))
            manager.getActiveContainerInstances().get(containerInstanceId).removePropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void syncContainerInstanceProperties(UUID containerInstanceId, String containerState) {
        if (manager.getActiveContainerInstances().containsKey(containerInstanceId))
            manager.getActiveContainerInstances().get(containerInstanceId).syncWithRealObject(containerState);
        else
            throw new NoSuchElementException("The container " + containerInstanceId + " does not exist.");
    }

    public static void subscribeNodeProperties(UUID nodeId, PropertyChangeListener pcl) {
        if (manager.getAvailableNodes().containsKey(nodeId))
            manager.getAvailableNodes().get(nodeId).addPropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The node " + nodeId + " does not exist.");
    }

    public static void unsubscribeNodeProperties(UUID nodeId, PropertyChangeListener pcl) {
        if (manager.getAvailableNodes().containsKey(nodeId))
            manager.getAvailableNodes().get(nodeId).removePropertyChangeListener(pcl);
        else
            throw new NoSuchElementException("The node " + nodeId + " does not exist.");
    }

    public static void syncNodeProperties(UUID nodeId, float memoryUsagePercentage, float cpuUsagePercentage) {
        if (manager.getAvailableNodes().containsKey(nodeId))
            manager.getAvailableNodes().get(nodeId).syncWithRealObject(memoryUsagePercentage, cpuUsagePercentage);
        else
            throw new NoSuchElementException("The node " + nodeId + " does not exist.");
    }
}
