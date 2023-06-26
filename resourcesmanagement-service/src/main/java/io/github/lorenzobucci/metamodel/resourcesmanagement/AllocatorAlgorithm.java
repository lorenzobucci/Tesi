package io.github.lorenzobucci.metamodel.resourcesmanagement;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateServiceContainer(ServiceRequirements serviceRequirements,
                                               UserRequirements userRequirements,
                                               Set<Node> availableNodes,
                                               Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(ServiceRequirements serviceRequirements,
                           UserRequirements userRequirements,
                           Set<Node> availableNodes,
                           Set<ContainerType> providedContainerTypes);
}
