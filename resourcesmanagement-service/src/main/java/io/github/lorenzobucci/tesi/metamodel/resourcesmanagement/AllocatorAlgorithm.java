package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.DependabilityRequirements;
import io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model.Node;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateContainer(DependabilityRequirements dependabilityRequirements,
                                        Set<Node> availableNodes,
                                        Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(DependabilityRequirements dependabilityRequirements,
                           ContainerType runningContainerType,
                           Set<Node> availableNodes);
}
