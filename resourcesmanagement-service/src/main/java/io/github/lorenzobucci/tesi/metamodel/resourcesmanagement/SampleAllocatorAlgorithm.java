package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateServiceContainer(DependabilityRequirements dependabilityRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        Node selectedNode = availableNodes.iterator().next();
        return new ContainerInstance(providedContainerTypes.iterator().next(), selectedNode.getId(), selectedNode.getIpAddress());
    }

    @Override
    public Node reviseOptimalNode(DependabilityRequirements dependabilityRequirements, ContainerType runningContainerType, Set<Node> availableNodes) {
        return availableNodes.stream().min(Comparator.comparingInt(Node::getOwnedContainersSize)).orElse(null);
    }
}
