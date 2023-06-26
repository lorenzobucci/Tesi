package io.github.lorenzobucci.metamodel.servicesmanagement;

import io.github.lorenzobucci.metamodel.resourcesmanagement.AllocationManager;
import io.github.lorenzobucci.metamodel.resourcesmanagement.UserRequirements;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public class ServiceGateway {

    private static ServiceGateway instance = null;
    private final ServiceManager serviceManager = ServiceManager.getInstance();

    private final AllocationManager allocationManager = AllocationManager.getInstance(); // TODO: USE API

    private ServiceGateway() {

    }

    public static ServiceGateway getInstance() {
        if (instance == null)
            instance = new ServiceGateway();
        return instance;
    }

    public WorkflowInstance instantiateWorkflow(UUID workflowTypeId, UserRequirements requirements) {
        // TODO: check client permissions, location, ...

        WorkflowInstance workflowInstance = serviceManager.instantiateWorkflow(workflowTypeId);
        workflowInstance.setUserRequirements(requirements);

        for (ServiceInstance serviceInstance : workflowInstance.serviceInstanceDAG.vertexSet()) {
            serviceInstance.hostContainer = allocationManager.allocateServiceContainer(
                    serviceInstance.getServiceRequirements(), serviceInstance.getUserRequirements()); // TODO: USE API
            serviceInstance.hostContainer.setServiceInstanceId(serviceInstance.id);
        }

        return workflowInstance;
    }

    public void updateWorkflowRequirements(UUID workflowInstanceId, UserRequirements newWorkflowRequirements) {
        if (serviceManager.runningWorkflowInstances.containsKey(workflowInstanceId)) {
            WorkflowInstance workflowInstance = serviceManager.runningWorkflowInstances.get(workflowInstanceId);
            workflowInstance.setUserRequirements(newWorkflowRequirements);

            for (ServiceInstance serviceInstance : workflowInstance.serviceInstanceDAG.vertexSet())
                allocationManager.reviseContainerAllocation(
                        serviceInstance.hostContainer.id, serviceInstance.getServiceRequirements(), serviceInstance.getUserRequirements()); // TODO: USE API
        } else
            throw new NoSuchElementException("Requested workflow " + workflowInstanceId + " does not exist.");
    }

    public Set<WorkflowType> getAvailableWorkflowTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        // dummy return
        Set<WorkflowType> availableWorkflowTypes = new HashSet<>();
        for (WorkflowType workflowType : serviceManager.providedWorkflowTypes.values())
            availableWorkflowTypes.add(new WorkflowType(workflowType));
        return availableWorkflowTypes;
    }

}