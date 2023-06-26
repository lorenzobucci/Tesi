package io.github.lorenzobucci.metamodel.servicesmanagement;

import java.util.*;

public class ServiceManager {

    private static ServiceManager instance = null;

    final Map<UUID, ServiceType> providedServiceTypes = new HashMap<>();
    final Map<UUID, WorkflowType> providedWorkflowTypes = new HashMap<>();

    final Map<UUID, WorkflowInstance> runningWorkflowInstances = new HashMap<>();

    private ServiceManager() {

    }

    public static ServiceManager getInstance() {
        if (instance == null)
            instance = new ServiceManager();
        return instance;
    }

    public void init(Set<ServiceType> providedServiceTypes, Set<WorkflowType> providedWorkflowTypes) {
        if (this.providedServiceTypes.isEmpty() && this.providedWorkflowTypes.isEmpty()) {
            for (ServiceType serviceType : providedServiceTypes)
                addNewServiceType(serviceType);
            for (WorkflowType workflowType : providedWorkflowTypes)
                addNewWorkflowType(workflowType);
        } else
            throw new IllegalStateException("Cannot initialize if there are services or workflows in memory.");
    }


    public void addNewServiceType(ServiceType serviceType) {
        if (!providedServiceTypes.containsKey(serviceType.id))
            providedServiceTypes.putIfAbsent(serviceType.id, new ServiceType(serviceType));
        else
            throw new IllegalArgumentException("The service " + serviceType.id + " is already in memory.");
    }

    public void addNewWorkflowType(WorkflowType workflowType) {
        if (!providedWorkflowTypes.containsKey(workflowType.id)) {
            WorkflowType newWorkflowType = new WorkflowType(workflowType);
            for (ServiceType serviceType : newWorkflowType.getServiceTypes())
                providedServiceTypes.putIfAbsent(serviceType.id, serviceType);
            providedWorkflowTypes.put(newWorkflowType.id, newWorkflowType);
        } else
            throw new IllegalArgumentException("The workflow " + workflowType.id + " is already in memory.");
    }

    public void cleanOrphanedServiceTypes() {
        for (UUID serviceTypeId : providedServiceTypes.keySet()) {
            try {
                removeServiceType(serviceTypeId);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public void removeServiceType(UUID serviceTypeId) {
        for (WorkflowType workflowType : providedWorkflowTypes.values()) {
            if (workflowType.contains(serviceTypeId))
                throw new IllegalStateException("The service " + serviceTypeId + " belongs to an existent workflow.");
        }
        providedServiceTypes.remove(serviceTypeId);
    }

    public void removeWorkflowType(UUID workflowTypeId) {
        providedWorkflowTypes.remove(workflowTypeId);
    }

    public ServiceType getServiceType(UUID serviceTypeId) {
        if (providedServiceTypes.containsKey(serviceTypeId))
            return new ServiceType(providedServiceTypes.get(serviceTypeId));
        else
            throw new NoSuchElementException("Requested service " + serviceTypeId + " does not exist.");
    }

    public WorkflowType getWorkflowType(UUID workflowTypeId) {
        if (providedWorkflowTypes.containsKey(workflowTypeId))
            return new WorkflowType(providedWorkflowTypes.get(workflowTypeId));
        else
            throw new NoSuchElementException("Requested workflow " + workflowTypeId + " does not exist.");
    }

    WorkflowInstance instantiateWorkflow(UUID workflowTypeId) {
        if (providedWorkflowTypes.containsKey(workflowTypeId)) {
            WorkflowInstance workflowInstance = new WorkflowInstance(providedWorkflowTypes.get(workflowTypeId));
            runningWorkflowInstances.put(workflowInstance.id, workflowInstance);
            return workflowInstance;
        } else
            throw new NoSuchElementException("Requested workflow " + workflowTypeId + " does not exist.");
    }


}
