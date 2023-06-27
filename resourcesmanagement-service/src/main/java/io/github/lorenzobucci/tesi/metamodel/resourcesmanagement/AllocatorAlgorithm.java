package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateServiceContainer(ServiceRequirements serviceRequirements,
                                               UserRequirements userRequirements,
                                               Set<Node> availableNodes,
                                               Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(ServiceRequirements serviceRequirements,
                           UserRequirements userRequirements,
                           ContainerType runningContainerType,
                           Set<Node> availableNodes);
}
