package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.Set;

public interface AllocatorAlgorithm {
    ContainerInstance allocateService(ServiceInstance serviceToAllocate,
                                      Set<Node> availableNodes,
                                      Set<ContainerType> providedContainerTypes);

    Node reviseOptimalNode(ServiceInstance changedService,
                           Set<Node> availableNodes,
                           Set<ContainerType> providedContainerTypes);
}
