package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public ContainerInstance allocateService(ServiceInstance serviceToAllocate, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        return new ContainerInstance(providedContainerTypes.iterator().next(), availableNodes.iterator().next().id, serviceToAllocate);
    }

    @Override
    public Node reviseOptimalNode(ServiceInstance changedService, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        return availableNodes.stream().min(Comparator.comparingInt(node -> node.getOwnedContainers().size())).orElse(null);
    }
}
