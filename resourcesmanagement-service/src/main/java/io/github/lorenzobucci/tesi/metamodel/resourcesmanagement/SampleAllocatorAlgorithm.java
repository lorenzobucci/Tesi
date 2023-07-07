package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.Node;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateContainer(DependabilityRequirements dependabilityRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        Node selectedNode = availableNodes.iterator().next();
        return new ContainerInstance(providedContainerTypes.iterator().next(), selectedNode);
    }

    @Override
    public Node reviseOptimalNode(DependabilityRequirements dependabilityRequirements, ContainerType runningContainerType, Set<Node> availableNodes) {
        return availableNodes.stream().min(Comparator.comparingInt(Node::getOwnedContainersSize)).orElse(null);
    }
}
