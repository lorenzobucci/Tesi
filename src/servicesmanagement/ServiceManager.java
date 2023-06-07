package servicesmanagement;

import java.util.HashSet;
import java.util.Set;

public class ServiceManager {

    private static ServiceManager instance = null;

    private final HashSet<ServiceType> allServiceTypes = new HashSet<>();
    private final HashSet<WorkflowType> allWorkflowTypes = new HashSet<>();

    private final HashSet<ServiceInstance> runningServiceInstances = new HashSet<>();
    private final HashSet<WorkflowInstance> runningWorkflowInstances = new HashSet<>();

    private ServiceManager() {

    }

    public static ServiceManager getInstance() {
        if (instance == null)
            instance = new ServiceManager();
        return instance;
    }


    public void addServiceTypes(Set<ServiceType> serviceTypes) {
        allServiceTypes.addAll(serviceTypes);
    }

    public void addWorkflowTypes(Set<WorkflowType> workflowTypes) {
        HashSet<ServiceType> serviceTypes = new HashSet<>();
        for (WorkflowType workflowType: workflowTypes)
            serviceTypes.addAll(workflowType.getServiceTypes());

        allServiceTypes.addAll(serviceTypes);
        allWorkflowTypes.addAll(workflowTypes);
    }

    public void removeServiceType(ServiceType serviceType) {
        allServiceTypes.remove(serviceType);
    }

    public void removeWorkflowType(WorkflowType workflowType) {
        allWorkflowTypes.remove(workflowType);
    }

    ServiceInstance instantiateService(ServiceType serviceType) {
        ServiceInstance serviceInstance = new ServiceInstance(serviceType);
        runningServiceInstances.add(serviceInstance);
        return serviceInstance;
    }

    WorkflowInstance instantiateWorkflow(WorkflowType workflowType) {
        WorkflowInstance workflowInstance = new WorkflowInstance(workflowType);
        runningWorkflowInstances.add(workflowInstance);
        runningServiceInstances.addAll(workflowInstance.getServiceInstances());
        return workflowInstance;
    }

    public HashSet<ServiceType> getAllServiceTypes() {
        return new HashSet<>(allServiceTypes);
    }

    public HashSet<WorkflowType> getAllWorkflowTypes() {
        return new HashSet<>(allWorkflowTypes);
    }


}
