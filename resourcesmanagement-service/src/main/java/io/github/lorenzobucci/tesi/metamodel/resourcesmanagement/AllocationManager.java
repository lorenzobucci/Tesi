package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.net.InetAddress;
import java.util.*;

public class AllocationManager {
    private static AllocationManager instance = null;

    private final Map<UUID, Node> availableNodes = new HashMap<>();

    private final Map<UUID, ContainerType> providedContainerTypes = new HashMap<>();
    private final Map<UUID, ContainerInstance> activeContainerInstances = new HashMap<>();

    private AllocatorAlgorithm allocator = null;

    private AllocationManager() {
    }

    public static AllocationManager getInstance() {
        if (instance == null)
            instance = new AllocationManager();
        return instance;
    }

    public void init(Set<Node> nodes, Set<ContainerType> containerTypes, AllocatorAlgorithm allocatorAlgorithm) {
        if (this.availableNodes.isEmpty() && providedContainerTypes.isEmpty()) {
            for (Node node : nodes)
                addNode(node);
            for (ContainerType containerType : containerTypes)
                addContainerType(containerType);
            allocator = allocatorAlgorithm;
        } else
            throw new IllegalStateException("Cannot initialize if there are nodes or containers in memory.");
    }

    public void addNode(Node node) {
        if (!availableNodes.containsKey(node.getId()))
            availableNodes.putIfAbsent(node.getId(), node);
        else
            throw new IllegalArgumentException("The node " + node.getId() + " is already in memory.");
    }

    public void removeNode(UUID nodeId) {
        availableNodes.remove(nodeId);
    }

    public void addContainerType(ContainerType containerType) {
        if (!providedContainerTypes.containsKey(containerType.getId()))
            providedContainerTypes.putIfAbsent(containerType.getId(), containerType);
        else
            throw new IllegalArgumentException("The container " + containerType.getId() + " is already in memory.");
    }

    public void removeContainerType(UUID containerTypeId) {
        providedContainerTypes.remove(containerTypeId);
    }

    public InetAddress allocateContainer(UUID associatedServiceId, String dependabilityRequirements) {
        if (allocator != null) {
            DependabilityRequirements requirements = new DependabilityRequirements(); // PARSE JSON

            ContainerInstance containerInstance = allocator.allocateServiceContainer(
                    requirements,
                    new HashSet<>(availableNodes.values()),
                    new HashSet<>(providedContainerTypes.values()));
            Node selectedNode = availableNodes.get(containerInstance.getBelongingNodeId());
            containerInstance.setAssociatedServiceId(associatedServiceId);

            activeContainerInstances.put(containerInstance.getId(), containerInstance);

            selectedNode.addOwnedContainer(containerInstance);

            return containerInstance.getNodeIpAddress();
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public InetAddress reviseContainerAllocation(UUID associatedServiceId, String newDependabilityRequirements) {
        if (allocator != null) {
            ContainerInstance container;
            try {
                container = activeContainerInstances.values().stream().
                        filter(containerInstance -> containerInstance.getAssociatedServiceId().equals(associatedServiceId)).findAny().orElseThrow();
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("There is no active allocation containing the service " + associatedServiceId);
            }
            if (!container.getContainerState().equals("TERMINATED")) { // or anything else
                Node oldNode = availableNodes.get(container.getBelongingNodeId());

                DependabilityRequirements requirements = new DependabilityRequirements(); // PARSE JSON

                Node newNode = allocator.reviseOptimalNode(
                        requirements,
                        container.getContainerType(),
                        new HashSet<>(availableNodes.values()));
                if (!availableNodes.containsKey(newNode.getId()))
                    throw new RuntimeException("The allocation algorithm returned a non-existent node");

                if (!oldNode.equals(newNode)) {
                    // CONTAINER MIGRATION
                    String previousContainerState = container.getContainerState();
                    container.setContainerState("SUSPENDED");
                    try {
                        container.acquireMigrationSemaphore();
                        try {
                            newNode.addOwnedContainer(container);
                            container.setBelongingNodeId(newNode.getId());
                            oldNode.removeOwnedContainer(container);
                            container.setNodeIpAddress(newNode.getIpAddress());
                        } finally {
                            container.releaseMigrationSemaphore();
                        }
                    } catch (InterruptedException ignored) {
                    } finally {
                        container.setContainerState(previousContainerState);
                    }
                }
            }
            return container.getNodeIpAddress();
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public void cleanInactiveContainers() {
        for (ContainerInstance containerInstance : activeContainerInstances.values()) {
            if (containerInstance.getContainerState().equals("TERMINATED")) {  // or anything else
                availableNodes.get(containerInstance.getBelongingNodeId()).removeOwnedContainer(containerInstance);
                activeContainerInstances.remove(containerInstance.getId());
            }
        }
    }

    public Map<UUID, Node> getAvailableNodes() {
        return availableNodes;
    }

    public Map<UUID, ContainerType> getProvidedContainerTypes() {
        return providedContainerTypes;
    }

    public Map<UUID, ContainerInstance> getActiveContainerInstances() {
        return activeContainerInstances;
    }

    public AllocatorAlgorithm getAllocator() {
        return allocator;
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}
