package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import org.json.JSONObject;

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
        workflowEndpoint.serviceParameters = serviceParameters;

        for (ServiceInstance serviceInstance : workflowInstance.serviceInstanceDAG.vertexSet()) {
            String dependabilityRequirements = serviceInstance.serviceType.requirements.toString() + userRequirements; // MERGE SERVICE REQUIREMENTS + USER REQUIREMENTS
            InetAddress containerIp = AllocationManager.getInstance().allocateContainer(serviceInstance.id, dependabilityRequirements); // TODO: USE API
            serviceInstance.determineBaseUri(containerIp);
        }

        return workflowEndpoint.baseUri; // TODO: USE API
    }

    public URI updateServiceRequirements(UUID runningServiceId, String newUserRequirements) {
        if (serviceManager.runningWorkflowInstances.containsKey(runningServiceId)) {
            WorkflowInstance workflowInstance = serviceManager.runningWorkflowInstances.get(runningServiceId);

            for (ServiceInstance serviceInstance : workflowInstance.serviceInstanceDAG.vertexSet()) {
                String dependabilityRequirements = serviceInstance.serviceType.requirements.toString() + newUserRequirements; // MERGE SERVICE REQUIREMENTS + USER REQUIREMENTS
                InetAddress containerIp = AllocationManager.getInstance().reviseContainerAllocation(serviceInstance.id, dependabilityRequirements); // TODO: USE API
                serviceInstance.determineBaseUri(containerIp);
            }

            return workflowInstance.getRootService().baseUri;
        } else
            throw new NoSuchElementException("Requested workflow " + runningServiceId + " does not exist.");
    }

}
