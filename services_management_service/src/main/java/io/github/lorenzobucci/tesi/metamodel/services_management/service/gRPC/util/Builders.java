package io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.util;

import io.github.lorenzobucci.tesi.metamodel.services_management.model.*;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.ServicesManagement;

public class Builders {

    private Builders() {
    }

    public static ServicesManagement.EndpointServiceTypeDTO buildEndpointServiceTypeDTO(EndpointServiceType endpointServiceType) {
        return ServicesManagement.EndpointServiceTypeDTO.newBuilder()
                .setId(endpointServiceType.getId())
                .setName(endpointServiceType.getName())
                .setServiceRequirements(buildServiceRequirementsDTO(endpointServiceType.getRequirements()))
                .setPhysicalEndpointURI(endpointServiceType.getPhysicalEndpointURI().toString())
                .build();
    }

    public static ServicesManagement.EndpointServiceInstanceDTO buildEndpointServiceInstanceDTO(EndpointServiceInstance endpointServiceInstance) {
        return ServicesManagement.EndpointServiceInstanceDTO.newBuilder()
                .setId(endpointServiceInstance.getId())
                .setBelongingWorkflow(buildWorkflowInstanceDTO(endpointServiceInstance.getBelongingWorkflow()))
                .setEndpointServiceType(buildEndpointServiceTypeDTO(endpointServiceInstance.getEndpointServiceType()))
                .setParameters(endpointServiceInstance.getParameters())
                .build();
    }

    public static ServicesManagement.ServiceTypeDTO buildServiceTypeDTO(ServiceType serviceType) {
        return ServicesManagement.ServiceTypeDTO.newBuilder()
                .setId(serviceType.getId())
                .setName(serviceType.getName())
                .setServiceRequirements(buildServiceRequirementsDTO(serviceType.getRequirements()))
                .build();
    }

    public static ServicesManagement.ServiceInstanceDTO buildServiceInstanceDTO(ServiceInstance serviceInstance) {
        return ServicesManagement.ServiceInstanceDTO.newBuilder()
                .setId(serviceInstance.getId())
                .setServiceType(buildServiceTypeDTO(serviceInstance.getServiceType()))
                .setBelongingWorkflow(buildWorkflowInstanceDTO(serviceInstance.getBelongingWorkflow()))
                .setContainer(buildContainerDTO(serviceInstance.getContainer()))
                .build();
    }

    public static ServicesManagement.WorkflowTypeDTO buildWorkflowTypeDTO(WorkflowType workflowType) {
        ServicesManagement.WorkflowTypeDTO.Builder workflowTypeDTO = ServicesManagement.WorkflowTypeDTO.newBuilder().setId(workflowType.getId());

        for (ServiceType serviceType : workflowType.getServiceTypes())
            workflowTypeDTO.addServiceTypeSet(buildServiceTypeDTO(serviceType));

        workflowTypeDTO.setEndpointServiceType(buildEndpointServiceTypeDTO(workflowType.getEndpointServiceType()));

        return workflowTypeDTO.build();
    }

    public static ServicesManagement.WorkflowInstanceDTO buildWorkflowInstanceDTO(WorkflowInstance workflowInstance) {
        ServicesManagement.WorkflowInstanceDTO.Builder workflowInstanceDTO = ServicesManagement.WorkflowInstanceDTO.newBuilder().setId(workflowInstance.getId());

        for (ServiceInstance serviceInstance : workflowInstance.getServiceInstances())
            workflowInstanceDTO.addServiceInstanceSet(buildServiceInstanceDTO(serviceInstance));

        workflowInstanceDTO.setEndpointServiceInstance(buildEndpointServiceInstanceDTO(workflowInstance.getEndpointServiceInstance()))
                .setRefWorkflowType(buildWorkflowTypeDTO(workflowInstance.getWorkflowType()))
                .setWorkflowRequirements(buildWorkflowRequirementsDTO(workflowInstance.getWorkflowRequirements()));

        return workflowInstanceDTO.build();
    }

    public static ServicesManagement.ContainerDTO buildContainerDTO(Container container) {
        return ServicesManagement.ContainerDTO.newBuilder()
                .setIpAddress(container.getIpAddress().toString())
                .setAssociatedContainerId(container.getAssociatedContainerId())
                .build();
    }

    public static ServicesManagement.ServiceRequirementsDTO buildServiceRequirementsDTO(ServiceRequirements serviceRequirements) {
        return ServicesManagement.ServiceRequirementsDTO.newBuilder().build(); // DO CONVERSION TO DTO
    }

    public static ServiceRequirements buildServiceRequirements(ServicesManagement.ServiceRequirementsDTO serviceRequirementsDTO) {
        return new ServiceRequirements(); // DO CONVERSION FROM DTO
    }

    public static ServicesManagement.WorkflowRequirementsDTO buildWorkflowRequirementsDTO(WorkflowRequirements workflowRequirements) {
        return ServicesManagement.WorkflowRequirementsDTO.newBuilder().build(); // DO CONVERSION TO DTO
    }

    public static WorkflowRequirements buildWorkflowRequirements(ServicesManagement.WorkflowRequirementsDTO workflowRequirementsDTO) {
        return new WorkflowRequirements(); // DO CONVERSION FROM DTO
    }
}
