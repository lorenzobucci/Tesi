package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.*;
import java.util.stream.Collectors;

public class AllocationManager {
    private static AllocationManager instance = null;

    final Map<UUID, Node> availableNodes = new HashMap<>();

    final Map<UUID, ContainerType> providedContainerTypes = new HashMap<>();
    final Set<ContainerInstance> activeContainerInstances = new HashSet<>();

    private AllocatorAlgorithm allocator = null;

    private AllocationManager() {
    }

    public static AllocationManager getInstance() {
        if (instance == null)
            instance = new AllocationManager();
        return instance;
    }

    public void init(Set<Node> nodes, Set<ContainerType> containerTypes, AllocatorAlgorithm allocatorAlgorithm) {
        if (this.availableNodes.isEmpty() && providedContainerTypes.isEmpty()) {
            for (Node node : nodes)
                addNode(node);
            for (ContainerType containerType : containerTypes)
                addContainerType(containerType);
            allocator = allocatorAlgorithm;
        } else
            throw new IllegalStateException("Cannot initialize if there are nodes or containers in memory");
    }

    public void addNode(Node node) {
        if (!availableNodes.containsKey(node.id))
            availableNodes.putIfAbsent(node.id, new Node(node));
        else
            throw new IllegalArgumentException("The node " + node.id + " is already in memory");
    }

    public void removeNode(UUID nodeId) {
        availableNodes.remove(nodeId);
    }

    public void addContainerType(ContainerType containerType) {
        if (!providedContainerTypes.containsKey(containerType.id))
            providedContainerTypes.putIfAbsent(containerType.id, new ContainerType(containerType));
        else
            throw new IllegalArgumentException("The container " + containerType.id + " is already in memory");
    }

    public void removeContainerType(UUID containerTypeId) {
        providedContainerTypes.remove(containerTypeId);
    }

    public void allocateService(ServiceInstance service) {
        if (allocator != null) {
            ContainerInstance containerInstance = allocator.allocateService(
                    service,
                    getAvailableNodes(),
                    getProvidedContainerTypes());
            activeContainerInstances.add(containerInstance);

            Node selectedNode = availableNodes.get(containerInstance.belongingNodeId);
            selectedNode.ownedContainer.add(containerInstance);
            service.nodeIpAddress = selectedNode.ipAddress;
        } else
            throw new IllegalStateException("The allocation algorithm has not yet been set");
    }

    public Map<UUID, Node> getAvailableNodes() {
        return availableNodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Node(e.getValue())));
    }

    public Map<UUID, ContainerType> getProvidedContainerTypes() {
        return providedContainerTypes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ContainerType(e.getValue())));
    }

    public Set<ContainerInstance> getActiveContainerInstances() {
        return new HashSet<>(activeContainerInstances);
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}
