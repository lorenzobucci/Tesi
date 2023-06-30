package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import java.net.InetAddress;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ServiceProxy {

    private static ServiceProxy instance = null;
    private final ServiceManager serviceManager = ServiceManager.getInstance();

    private ServiceProxy() {

    }

    public static ServiceProxy getInstance() {
        if (instance == null)
            instance = new ServiceProxy();
        return instance;
    }

    public URI requestService(UUID serviceId, String serviceParameters, String userRequirements) {
        // TODO: check client permissions, location, ...

        WorkflowInstance workflowInstance = serviceManager.instantiateWorkflow(serviceId);

        ServiceInstance workflowEndpoint = workflowInstance.getRootService();
        workflowEndpoint.setServiceParameters(serviceParameters);

        for (ServiceInstance serviceInstance : workflowInstance.getDAG().vertexSet()) {
            String dependabilityRequirements = serviceInstance.getServiceType().getRequirements().toString() + userRequirements; // MERGE SERVICE REQUIREMENTS + USER REQUIREMENTS
            InetAddress containerIp = AllocationManager.getInstance().allocateContainer(serviceInstance.getId(), dependabilityRequirements); // TODO: USE API
            serviceInstance.determineBaseUri(containerIp);
        }

        return workflowEndpoint.getBaseUri(); // TODO: USE API AND RETURN ALSO WORKFLOW INSTANCE ID AKA "RUNNING SERVICE ID"
    }

    public URI updateServiceRequirements(UUID runningServiceId, String newUserRequirements) {
        if (serviceManager.getRunningWorkflowInstances().containsKey(runningServiceId)) {
            WorkflowInstance workflowInstance = serviceManager.getRunningWorkflowInstances().get(runningServiceId);

            for (ServiceInstance serviceInstance : workflowInstance.getDAG().vertexSet()) {
                String dependabilityRequirements = serviceInstance.getServiceType().getRequirements().toString() + newUserRequirements; // MERGE SERVICE REQUIREMENTS + USER REQUIREMENTS
                InetAddress containerIp = AllocationManager.getInstance().reviseContainerAllocation(serviceInstance.getId(), dependabilityRequirements); // TODO: USE API
                serviceInstance.determineBaseUri(containerIp);
            }

            return workflowInstance.getRootService().getBaseUri();
        } else
            throw new NoSuchElementException("Requested workflow " + runningServiceId + " does not exist.");
    }

}
