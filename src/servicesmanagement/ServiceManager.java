package servicesmanagement;

import java.util.*;

public class ServiceManager {

    private static ServiceManager instance = null;

    private final HashMap<UUID, ServiceType> allServiceTypes = new HashMap<>();
    private final HashMap<UUID, WorkflowType> allWorkflowTypes = new HashMap<>();

    private final HashMap<UUID, ServiceInstance> runningServiceInstances = new HashMap<>();
    private final HashMap<UUID, WorkflowInstance> runningWorkflowInstances = new HashMap<>();

    private ServiceManager() {

    }

    public static ServiceManager getInstance() {
        if (instance == null)
            instance = new ServiceManager();
        return instance;
    }


    public void addNewServiceType(ServiceType serviceType) {
        if (!allServiceTypes.containsKey(serviceType.id))
            allServiceTypes.putIfAbsent(serviceType.id, new ServiceType(serviceType));
        else
            throw new IllegalArgumentException("The service is already in memory");
    }

    public void addNewWorkflowType(WorkflowType workflowType) {
        if (!allWorkflowTypes.containsKey(workflowType.id)) {
            WorkflowType newWorkflowType = new WorkflowType(workflowType);
            for (ServiceType serviceType : newWorkflowType.getServiceTypes())
                allServiceTypes.putIfAbsent(serviceType.id, serviceType);
            allWorkflowTypes.put(newWorkflowType.id, newWorkflowType);
        } else
            throw new IllegalArgumentException("The workflow is already in memory");
    }

    public void removeServiceType(UUID serviceTypeId) {
        for (WorkflowType workflowType : allWorkflowTypes.values()) {
            if (workflowType.contains(serviceTypeId))
                throw new IllegalArgumentException("The specified service belongs to an existent workflow");
        }
        allServiceTypes.remove(serviceTypeId);
    }

    public void removeWorkflowType(UUID workflowTypeId) {
        allWorkflowTypes.remove(workflowTypeId);
    }

    public ServiceType getServiceType(UUID serviceTypeId){
        return new ServiceType(allServiceTypes.get(serviceTypeId));
    }

    public WorkflowType getWorkflowType(UUID workflowTypeId) {
        return new WorkflowType(allWorkflowTypes.get(workflowTypeId));
    }

    ServiceInstance instantiateService(ServiceType serviceType) {
        ServiceInstance serviceInstance = new ServiceInstance(serviceType);
        runningServiceInstances.put(serviceInstance.id, serviceInstance);
        return serviceInstance;
    }

    WorkflowInstance instantiateWorkflow(WorkflowType workflowType) {
        WorkflowInstance workflowInstance = new WorkflowInstance(workflowType);
        runningWorkflowInstances.put(workflowInstance.id, workflowInstance);
        for (ServiceInstance serviceInstance : workflowInstance.getServiceInstances())
            runningServiceInstances.put(serviceInstance.id, serviceInstance);
        return workflowInstance;
    }

    HashMap<UUID, ServiceType> getAllServiceTypes() {
        return allServiceTypes;
    }

    HashMap<UUID, WorkflowType> getAllWorkflowTypes() {
        return allWorkflowTypes;
    }


}
