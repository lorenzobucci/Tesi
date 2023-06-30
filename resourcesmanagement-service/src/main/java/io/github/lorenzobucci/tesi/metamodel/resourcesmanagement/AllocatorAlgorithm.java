package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateServiceContainer(DependabilityRequirements dependabilityRequirements,
                                               Set<Node> availableNodes,
                                               Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(DependabilityRequirements dependabilityRequirements,
                           ContainerType runningContainerType,
                           Set<Node> availableNodes);
}
