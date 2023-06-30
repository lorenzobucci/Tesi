package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

public class AllocationManager {
    private static AllocationManager instance = null;

    final Map<UUID, Node> availableNodes = new HashMap<>();

    final Map<UUID, ContainerType> providedContainerTypes = new HashMap<>();
    final Map<UUID, ContainerInstance> activeContainerInstances = new HashMap<>();

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
        if (!availableNodes.containsKey(node.id))
            availableNodes.putIfAbsent(node.id, new Node(node));
        else
            throw new IllegalArgumentException("The node " + node.id + " is already in memory.");
    }

    public void removeNode(UUID nodeId) {
        availableNodes.remove(nodeId);
    }

    public void addContainerType(ContainerType containerType) {
        if (!providedContainerTypes.containsKey(containerType.id))
            providedContainerTypes.putIfAbsent(containerType.id, new ContainerType(containerType));
        else
            throw new IllegalArgumentException("The container " + containerType.id + " is already in memory.");
    }

    public void removeContainerType(UUID containerTypeId) {
        providedContainerTypes.remove(containerTypeId);
    }

    public InetAddress allocateContainer(UUID associatedServiceId, String dependabilityRequirements) {
        if (allocator != null) {
            DependabilityRequirements requirements = new DependabilityRequirements(); // PARSE JSON

            ContainerInstance containerInstance = allocator.allocateServiceContainer(
                    requirements,
                    new HashSet<>(getAvailableNodes().values()),
                    new HashSet<>(getProvidedContainerTypes().values()));
            Node selectedNode = availableNodes.get(containerInstance.belongingNodeId);

            activeContainerInstances.put(containerInstance.id, containerInstance);

            selectedNode.addOwnedContainer(containerInstance);

            return containerInstance.nodeIpAddress;
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public InetAddress reviseContainerAllocation(UUID associatedServiceId, String newDependabilityRequirements) {
        if (allocator != null) {
            ContainerInstance container;
            try {
                container = activeContainerInstances.values().stream().
                        filter(containerInstance -> containerInstance.associatedServiceId.equals(associatedServiceId)).findAny().orElseThrow();
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("There is no active allocation containing the service " + associatedServiceId);
            }
            if (!container.getContainerState().equals("TERMINATED")) { // or anything else
                Node oldNode = availableNodes.get(container.belongingNodeId);

                DependabilityRequirements requirements = new DependabilityRequirements(); // PARSE JSON

                Node returnedNewNode = allocator.reviseOptimalNode(
                        requirements,
                        new ContainerType(container.containerType),
                        new HashSet<>(getAvailableNodes().values()));
                if (!availableNodes.containsKey(returnedNewNode.id))
                    throw new RuntimeException("The allocation algorithm returned a non-existent node");
                Node newNode = availableNodes.get(returnedNewNode.id);

                if (!oldNode.equals(newNode)) {
                    // CONTAINER MIGRATION
                    String previousContainerState = container.getContainerState();
                    container.setContainerState("SUSPENDED");
                    try {
                        container.acquireMigrationSemaphore();
                        try {
                            newNode.addOwnedContainer(container);
                            container.belongingNodeId = newNode.id;
                            oldNode.removeOwnedContainer(container);
                            container.nodeIpAddress = newNode.ipAddress;
                        } finally {
                            container.releaseMigrationSemaphore();
                        }
                    } catch (InterruptedException ignored) {
                    } finally {
                        container.setContainerState(previousContainerState);
                    }
                }
            }
            return container.nodeIpAddress;
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public void cleanInactiveContainers() {
        for (ContainerInstance containerInstance : activeContainerInstances.values()) {
            if (containerInstance.getContainerState().equals("TERMINATED")) {  // or anything else
                availableNodes.get(containerInstance.belongingNodeId).removeOwnedContainer(containerInstance);
                activeContainerInstances.remove(containerInstance.id);
            }
        }
    }

    public Map<UUID, Node> getAvailableNodes() {
        return availableNodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Node(e.getValue())));
    }

    public Map<UUID, ContainerType> getProvidedContainerTypes() {
        return providedContainerTypes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ContainerType(e.getValue())));
    }

    public Map<UUID, ContainerInstance> getActiveContainerInstances() {
        return new HashMap<>(activeContainerInstances);
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}
