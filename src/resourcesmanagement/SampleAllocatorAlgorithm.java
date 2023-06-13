package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateService(ServiceInstance serviceToAllocate, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        return new ContainerInstance(providedContainerTypes.iterator().next(), availableNodes.iterator().next().id, serviceToAllocate);
    }
}
