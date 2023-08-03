package io.github.lorenzobucci.tesi.metamodel.resources_management.allocator;

import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public AllocateResponse allocateContainer(DependabilityRequirements dependabilityRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        Node selectedNode = availableNodes.iterator().next();
        ContainerInstance containerInstance = new ContainerInstance(providedContainerTypes.iterator().next());
        return new AllocateResponse(containerInstance, selectedNode);
    }

    @Override
    public Node reviseOptimalNode(ContainerInstance containerInstance, DependabilityRequirements newDependabilityRequirements, Set<Node> availableNodes) {
        return availableNodes.stream().min(Comparator.comparingInt(Node::getOwnedContainersSize)).orElse(null);
    }
}
