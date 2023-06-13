package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.util.*;
import java.util.stream.Collectors;

public class AllocationManager {
    private static AllocationManager instance = null;

    final Map<UUID, Node> availableNodes = new HashMap<>();

    final Map<UUID, ContainerType> providedContainerTypes = new HashMap<>();
    final Map<UUID, ContainerInstance> activeContainerInstances = new HashMap<>();

    final Map<Node, Set<ContainerInstance>> nodeContainersMap = new HashMap<>();

    private AllocatorAlgorithm allocator;

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
        ContainerInstance containerInstance = allocator.allocateService(
                service,
                getAvailableNodes(),
                getProvidedContainerTypes(),
                getActiveContainerInstances(),
                getNodeContainersMap());
        activeContainerInstances.put(containerInstance.id, containerInstance);

        Node selectedNode = availableNodes.get(containerInstance.belongingNodeId);
        if (nodeContainersMap.containsKey(selectedNode))
            nodeContainersMap.get(selectedNode).add(containerInstance);
        else
            nodeContainersMap.put(selectedNode, Collections.singleton(containerInstance));

        service.nodeIpAddress = selectedNode.ipAddress;
    }

    public void cleanInactiveContainerInstances() {
        for (ContainerInstance containerInstance : activeContainerInstances.values()) {
            if (containerInstance.state.equals("TERMINATED")) { // or anything else
                activeContainerInstances.remove(containerInstance.id);
                nodeContainersMap.get(availableNodes.get(containerInstance.belongingNodeId)).remove(containerInstance);
            }
        }
    }

    public Map<UUID, Node> getAvailableNodes() {
        return availableNodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Node(e.getValue())));
    }

    public Map<UUID, ContainerType> getProvidedContainerTypes() {
        return providedContainerTypes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ContainerType(e.getValue())));
    }

    public Map<UUID, ContainerInstance> getActiveContainerInstances() {
        return new HashMap<>(activeContainerInstances);
    }

    public Map<Node, Set<ContainerInstance>> getNodeContainersMap() {
        return nodeContainersMap.entrySet().stream().collect(Collectors.toMap(e -> new Node(e.getKey()), Map.Entry::getValue));
    }

    public void setAllocatorAlgorithm(AllocatorAlgorithm allocatorAlgorithm) {
        allocator = allocatorAlgorithm;
    }

}