package servicesmanagement;

import mobiledevice.UserRequirements;

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

    public WorkflowInstance instantiateWorkflow(UUID workflowTypeId, UserRequirements requirements) {
        // TODO: check client permissions, location, ...

        WorkflowInstance workflowInstance = serviceManager.instantiateWorkflow(workflowTypeId);
        workflowInstance.setUserRequirements(requirements);
        return workflowInstance;
    }

    public Set<WorkflowType> getAvailableWorkflowTypes(UUID clientID) {
        // TODO: check client permissions, location, ...

        // dummy return
        Set<WorkflowType> availableWorkflowTypes = new HashSet<>();
        for (WorkflowType workflowType: serviceManager.getProvidedWorkflowTypes().values())
            availableWorkflowTypes.add(new WorkflowType(workflowType));
        return availableWorkflowTypes;
    }

}
