package servicesmanagement;

import java.util.*;

public class ServiceManager {

    private static ServiceManager instance = null;

    private final HashMap<UUID, ServiceType> providedServiceTypes = new HashMap<>();
    private final HashMap<UUID, WorkflowType> providedWorkflowTypes = new HashMap<>();

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
        if (!providedServiceTypes.containsKey(serviceType.id))
            providedServiceTypes.putIfAbsent(serviceType.id, new ServiceType(serviceType));
        else
            throw new IllegalArgumentException("The service is already in memory");
    }

    public void addNewWorkflowType(WorkflowType workflowType) {
        if (!providedWorkflowTypes.containsKey(workflowType.id)) {
            WorkflowType newWorkflowType = new WorkflowType(workflowType);
            for (ServiceType serviceType : newWorkflowType.getServiceTypes())
                providedServiceTypes.putIfAbsent(serviceType.id, serviceType);
            providedWorkflowTypes.put(newWorkflowType.id, newWorkflowType);
        } else
            throw new IllegalArgumentException("The workflow is already in memory");
    }

    public void removeServiceType(UUID serviceTypeId) {
        for (WorkflowType workflowType : providedWorkflowTypes.values()) {
            if (workflowType.contains(serviceTypeId))
                throw new IllegalArgumentException("The specified service belongs to an existent workflow");
        }
        providedServiceTypes.remove(serviceTypeId);
    }

    public void removeWorkflowType(UUID workflowTypeId) {
        providedWorkflowTypes.remove(workflowTypeId);
    }

    public ServiceType getServiceType(UUID serviceTypeId) {
        return new ServiceType(providedServiceTypes.get(serviceTypeId));
    }

    public WorkflowType getWorkflowType(UUID workflowTypeId) {
        return new WorkflowType(providedWorkflowTypes.get(workflowTypeId));
    }

    ServiceInstance instantiateService(UUID serviceTypeId) {
        try {
            ServiceInstance serviceInstance = new ServiceInstance(providedServiceTypes.get(serviceTypeId));
            runningServiceInstances.put(serviceInstance.id, serviceInstance);
            return serviceInstance;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The specified service does not exist");
        }
    }

    WorkflowInstance instantiateWorkflow(UUID workflowTypeId) {
        try {
            WorkflowInstance workflowInstance = new WorkflowInstance(providedWorkflowTypes.get(workflowTypeId));
            runningWorkflowInstances.put(workflowInstance.id, workflowInstance);
            for (ServiceInstance serviceInstance : workflowInstance.getServiceInstances())
                runningServiceInstances.put(serviceInstance.id, serviceInstance);
            return workflowInstance;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The specified workflow does not exist");
        }
    }

    HashMap<UUID, ServiceType> getProvidedServiceTypes() {
        return providedServiceTypes;
    }

    HashMap<UUID, WorkflowType> getProvidedWorkflowTypes() {
        return providedWorkflowTypes;
    }


}
