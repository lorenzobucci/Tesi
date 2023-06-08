package servicesmanagement;

import java.util.HashSet;
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

    public WorkflowInstance instantiateWorkflow(WorkflowType workflowType) {
        return serviceManager.instantiateWorkflow(workflowType);
    }

    public HashSet<ServiceType> getAvailableServiceTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        return new HashSet<>(serviceManager.getAllServiceTypes()); // dummy return
    }

    public HashSet<WorkflowType> getAvailableWorkflowTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        return new HashSet<>(serviceManager.getAllWorkflowTypes()); // dummy return
    }

}
