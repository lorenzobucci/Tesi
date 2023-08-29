package io.github.lorenzobucci.tesi.resources_management.allocator;

import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.resources_management.model.Node;

import java.util.Comparator;
import java.util.Set;

public class SampleAllocatorAlgorithm implements AllocatorAlgorithm {

    @Override
    public AllocateResponse allocateContainer(DependabilityRequirements dependabilityRequirements, Set<Node> availableNodes, Set<ContainerType> providedContainerTypes) {
        Node selectedNode = null;

        if (dependabilityRequirements.proximityComputation()) {
            float minDistance = Float.POSITIVE_INFINITY;
            for (Node node : availableNodes) {
                float distance = geoDistance(dependabilityRequirements.preferredLatitude(), dependabilityRequirements.preferredLongitude(), node.getLatitude(), node.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedNode = node;
                }
            }
        } else
            selectedNode = availableNodes.iterator().next();

        ContainerInstance containerInstance = new ContainerInstance(providedContainerTypes.iterator().next());
        return new AllocateResponse(containerInstance, selectedNode);
    }

    @Override
    public Node reviseOptimalNode(ContainerInstance containerInstance, DependabilityRequirements newDependabilityRequirements, Set<Node> availableNodes) {
        Node selectedNode = null;

        if (newDependabilityRequirements.proximityComputation()) {
            float minDistance = Float.POSITIVE_INFINITY;
            for (Node node : availableNodes) {
                float distance = geoDistance(newDependabilityRequirements.preferredLatitude(), newDependabilityRequirements.preferredLongitude(), node.getLatitude(), node.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedNode = node;
                }
            }
        } else
            selectedNode = availableNodes.stream().min(Comparator.comparingInt(Node::getOwnedContainersSize)).orElse(null);

        return selectedNode;
    }

    // CALCULATION OF GEOGRAPHIC DISTANCE ALGORITHM
    private float geoDistance(float latitude1, float longitude1, float latitude2, float longitude2) {
        double radLatitude1 = Math.toRadians(latitude1);
        double radLongitude1 = Math.toRadians(longitude1);
        double radLatitude2 = Math.toRadians(latitude2);
        double radLongitude2 = Math.toRadians(longitude2);

        double longitudeDiff = radLongitude2 - radLongitude1;
        double latitudeDiff = radLatitude2 - radLatitude1;
        double a = Math.pow(Math.sin(latitudeDiff / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(longitudeDiff / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        return (float) (c * r);
    }
}
