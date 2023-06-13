package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AllocatorAlgorithm {
    ContainerInstance allocateService(ServiceInstance serviceToAllocate,
                                      Map<UUID, Node> availableNodes,
                                      Map<UUID, ContainerType> providedContainerTypes,
                                      Map<Node, Set<ContainerInstance>> nodeContainersMap);
}
