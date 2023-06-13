package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.Map;
import java.util.UUID;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateService(ServiceInstance serviceToAllocate, Map<UUID, Node> availableNodes, Map<UUID, ContainerType> providedContainerTypes) {
        return new ContainerInstance(providedContainerTypes.values().iterator().next(), availableNodes.values().iterator().next().id, serviceToAllocate);
    }
}
