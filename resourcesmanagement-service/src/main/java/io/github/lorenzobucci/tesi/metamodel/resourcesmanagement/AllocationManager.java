package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.Node;

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


    public InetAddress allocateContainer(UUID associatedServiceId, String dependabilityRequirements) {
        if (allocator != null) {
            DependabilityRequirements requirements = new DependabilityRequirements(); // PARSE JSON

            ContainerInstance containerInstance = allocator.allocateContainer(
                    requirements,
                    new HashSet<>(availableNodes.values()),
                    new HashSet<>(providedContainerTypes.values()));
            Node selectedNode = containerInstance.getBelongingNode();
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
                Node oldNode = container.getBelongingNode();

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
                            container.setBelongingNode(newNode);
                            oldNode.removeOwnedContainer(container);
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
                containerInstance.getBelongingNode().removeOwnedContainer(containerInstance);
                activeContainerInstances.remove(containerInstance.getId());
            }
        }
    }


    public AllocatorAlgorithm getAllocator() {
        return allocator;
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}
