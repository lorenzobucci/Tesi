package io.github.lorenzobucci.tesi.metamodel.resources_management;

import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateContainer(DependabilityRequirements dependabilityRequirements,
                                        Set<Node> availableNodes,
                                        Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(DependabilityRequirements dependabilityRequirements,
                           ContainerType runningContainerType,
                           Set<Node> availableNodes);
}
