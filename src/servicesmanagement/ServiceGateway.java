package servicesmanagement;

import java.util.HashSet;
import java.util.UUID;

public class ServiceGateway {

    private static ServiceGateway instance = null;
    private final ServiceManager serviceManager = ServiceManager.getInstance();

    private ServiceGateway() {

    }

    public WorkflowInstance instantiateWorkflow(WorkflowType workflowType) {
        return serviceManager.instantiateWorkflow(workflowType);
    }

    public static ServiceGateway getInstance() {
        if (instance == null)
            instance = new ServiceGateway();
        return instance;
    }

    public HashSet<WorkflowType> getAvailableWorkflowTypes(UUID clientID) {
        // check client permissions, location, ...

        return new HashSet<>(serviceManager.getAllWorkflowTypes()); // dummy return
    }

}
