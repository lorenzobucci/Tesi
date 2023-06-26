package io.github.lorenzobucci.metamodel.resourcesmanagement;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateServiceContainer(ServiceRequirements serviceRequirements, UserRequirements userRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        return new ContainerInstance(providedContainerTypes.iterator().next(), availableNodes.iterator().next().id);
    }

    @Override
    public Node reviseOptimalNode(ServiceRequirements serviceRequirements, UserRequirements userRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        return availableNodes.stream().min(Comparator.comparingInt(node -> node.getOwnedContainers().size())).orElse(null);
    }
}
