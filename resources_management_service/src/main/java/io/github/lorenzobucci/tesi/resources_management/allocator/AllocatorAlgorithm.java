package io.github.lorenzobucci.tesi.resources_management.allocator;

import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.resources_management.model.Node;

import java.util.Set;

public interface AllocatorAlgorithm {
    AllocateResponse allocateContainer(DependabilityRequirements dependabilityRequirements,
                                        Set<Node> availableNodes,
                                        Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(ContainerInstance containerInstance,
                           DependabilityRequirements newDependabilityRequirements,
                           Set<Node> availableNodes);

    record AllocateResponse(ContainerInstance createdContainerInstance, Node selectedNode) {
    }
}