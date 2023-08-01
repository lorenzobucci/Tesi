package io.github.lorenzobucci.tesi.metamodel.resources_management.controller;

import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.AllocatorAlgorithm;
import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.DependabilityRequirements;
import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.SampleAllocatorAlgorithm;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.NoSuchElementException;

@Singleton
public class AllocationController {

    @Inject
    private ContainerInstanceController containerInstanceController;

    @Inject
    private ContainerTypeController containerTypeController;

    @Inject
    private NodeController nodeController;

    private AllocatorAlgorithm allocator = new SampleAllocatorAlgorithm(); // DEFAULT ALGORITHM

    public ContainerInstance allocateContainer(DependabilityRequirements dependabilityRequirements) {
        ContainerInstance containerInstance = allocator.allocateContainer(dependabilityRequirements,
                new HashSet<>(nodeController.retrieveNodes()),
                new HashSet<>(containerTypeController.retrieveContainerTypes()));

        containerInstance = containerInstanceController.addContainerInstance(containerInstance);

        Node node = nodeController.getNode(containerInstance.getBelongingNode().getId());
        node.addOwnedContainer(containerInstance);
        nodeController.updateNode(node);

        return containerInstance;

    }

    public ContainerInstance reviseContainerAllocation(long containerInstanceId, DependabilityRequirements newDependabilityRequirements) throws NoSuchElementException {
        ContainerInstance containerInstance = containerInstanceController.getContainerInstance(containerInstanceId);
        Node oldNode = nodeController.getNode(containerInstance.getBelongingNode().getId());

        Node newNode = nodeController.getNode(allocator.reviseOptimalNode(
                containerInstance,
                newDependabilityRequirements,
                new HashSet<>(nodeController.retrieveNodes())).getId());

        if (!oldNode.equals(newNode)) {
            // CONTAINER MIGRATION
            newNode.addOwnedContainer(containerInstance);
            containerInstance.updateBelongingNode(newNode);
            oldNode.removeOwnedContainer(containerInstance);

            nodeController.updateNode(newNode);
            nodeController.updateNode(oldNode);
        }

        return containerInstance;
    }

    public AllocatorAlgorithm getCurrentAllocator() {
        return allocator;
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}
