package io.github.lorenzobucci.tesi.metamodel.resources_management.controller;

import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.AllocatorAlgorithm;
import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.DependabilityRequirements;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;
import jakarta.inject.Inject;

import java.util.HashSet;

public class AllocationController {

    @Inject
    private ContainerInstanceController containerInstanceController;

    @Inject
    private ContainerTypeController containerTypeController;

    @Inject
    private NodeController nodeController;

    private AllocatorAlgorithm allocator = null;

    public ContainerInstance allocateContainer(DependabilityRequirements dependabilityRequirements) throws IllegalStateException {
        if (allocator != null) {
            ContainerInstance containerInstance = allocator.allocateContainer(dependabilityRequirements,
                    new HashSet<>(nodeController.retrieveNodes()),
                    new HashSet<>(containerTypeController.retrieveContainerTypes()));

            containerInstance = containerInstanceController.addContainerInstance(containerInstance);

            containerInstance.getBelongingNode().addOwnedContainer(containerInstance);
            nodeController.updateNode(containerInstance.getBelongingNode());

            return containerInstance;
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public ContainerInstance reviseContainerAllocation(long containerInstanceId, DependabilityRequirements newDependabilityRequirements) throws IllegalStateException {
        if (allocator != null) {
            ContainerInstance containerInstance = containerInstanceController.getContainerInstance(containerInstanceId);
            Node oldNode = containerInstance.getBelongingNode();

            Node newNode = allocator.reviseOptimalNode(
                    containerInstance,
                    newDependabilityRequirements,
                    new HashSet<>(nodeController.retrieveNodes()));

            if (!oldNode.equals(newNode)) {
                // CONTAINER MIGRATION
                newNode.addOwnedContainer(containerInstance);
                containerInstance.updateBelongingNode(newNode);
                oldNode.removeOwnedContainer(containerInstance);

                nodeController.updateNode(newNode);
                nodeController.updateNode(oldNode);
            }

            return containerInstance;
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set.");
    }

    public AllocatorAlgorithm getCurrentAllocator() {
        return allocator;
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

    void deallocateContainer(ContainerInstance containerInstance) {
        containerInstance.getBelongingNode().removeOwnedContainer(containerInstance);
        nodeController.updateNode(containerInstance.getBelongingNode());
    }
}
