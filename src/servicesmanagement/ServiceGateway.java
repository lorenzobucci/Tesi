package servicesmanagement;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ServiceGateway {

    private static ServiceGateway instance = null;
    private final ServiceManager serviceManager = ServiceManager.getInstance();

    private ServiceGateway() {

    }

    public static ServiceGateway getInstance() {
        if (instance == null)
            instance = new ServiceGateway();
        return instance;
    }

    public ServiceInstance instantiateService(UUID serviceTypeId) {
        // TODO: check client permissions, location, ...

        return serviceManager.instantiateService(serviceTypeId);
    }

    public WorkflowInstance instantiateWorkflow(UUID workflowTypeId) {
        // TODO: check client permissions, location, ...

        return serviceManager.instantiateWorkflow(workflowTypeId);
    }

    public Set<ServiceType> getAvailableServiceTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        // dummy return
        HashSet<ServiceType> availableServiceTypes = new HashSet<>();
        for (ServiceType serviceType: serviceManager.getProvidedServiceTypes().values())
            availableServiceTypes.add(new ServiceType(serviceType));
        return availableServiceTypes;
    }

    public Set<WorkflowType> getAvailableWorkflowTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        // dummy return
        HashSet<WorkflowType> availableWorkflowTypes = new HashSet<>();
        for (WorkflowType workflowType: serviceManager.getProvidedWorkflowTypes().values())
            availableWorkflowTypes.add(new WorkflowType(workflowType));
        return availableWorkflowTypes;
    }

}
