package io.github.lorenzobucci.tesi.resources_management.service.gRPC.util;

import io.github.lorenzobucci.tesi.resources_management.allocator.DependabilityRequirements;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.resources_management.model.Node;
import io.github.lorenzobucci.tesi.resources_management.model.NodeTechnicalProperties;
import io.github.lorenzobucci.tesi.resources_management.service.gRPC.ResourcesManagement;

public class Builders {

    private Builders() {
    }

    public static ResourcesManagement.NodeDTO buildNodeDTO(Node node) {
        ResourcesManagement.NodeDTO.Builder nodeDTO = ResourcesManagement.NodeDTO.newBuilder().setId(node.getId())
                .setNodeType(ResourcesManagement.NodeTypeDTO.valueOf(node.getNodeType().toString()));

        for (ContainerInstance containerInstance : node.getOwnedContainers())
            nodeDTO.addOwnedContainerInstancesId(containerInstance.getId());

        nodeDTO.setProperties(buildNodeTechnicalPropertiesDTO(node.getProperties()))
                .setIpAddress(node.getIpAddress().getHostAddress())
                .setLatitude(node.getLatitude())
                .setLongitude(node.getLongitude());

        return nodeDTO.build();
    }

    public static ResourcesManagement.NodeTechnicalPropertiesDTO buildNodeTechnicalPropertiesDTO(NodeTechnicalProperties properties) {
        return ResourcesManagement.NodeTechnicalPropertiesDTO.newBuilder()
                .setMemoryMB(properties.getMemoryMB())
                .setCpuCoresNumber(properties.getCpuCoresNumber())
                .setCpuFrequencyGHz(properties.getCpuFrequencyGHz())
                .setInstalledOS(properties.getInstalledOS())
                .build();
    }

    public static ResourcesManagement.ContainerTypeDTO buildContainerTypeDTO(ContainerType containerType) {
        return ResourcesManagement.ContainerTypeDTO.newBuilder()
                .setId(containerType.getId())
                .setImageName(containerType.getImageName())
                .setImageVersion(containerType.getImageVersion())
                .build();
    }

    public static ResourcesManagement.ContainerInstanceDTO buildContainerInstanceDTO(ContainerInstance containerInstance) {
        return ResourcesManagement.ContainerInstanceDTO.newBuilder()
                .setId(containerInstance.getId())
                .setContainerTypeId(containerInstance.getContainerType().getId())
                .setContainerState(containerInstance.getContainerState())
                .setBelongingNodeId(containerInstance.getBelongingNode().getId())
                .setNodeIpAddress(containerInstance.getBelongingNode().getIpAddress().getHostAddress())
                .build();
    }

    public static DependabilityRequirements buildDependabilityRequirements(ResourcesManagement.DependabilityRequirementsDTO dependabilityRequirements) {
        return new DependabilityRequirements(); // DO CONVERSION FROM DTO
    }
}

