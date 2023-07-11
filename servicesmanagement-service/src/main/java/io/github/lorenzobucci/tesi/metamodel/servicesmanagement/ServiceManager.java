package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowInstance;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowType;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ServiceManager {

    private static ServiceManager instance = null;

    private final Map<UUID, ServiceType> providedServiceTypes = new HashMap<>();
    private final Map<UUID, WorkflowType> providedWorkflowTypes = new HashMap<>();

    private final Map<UUID, WorkflowInstance> runningWorkflowInstances = new HashMap<>();

    private ServiceManager() {

    }

    public static ServiceManager getInstance() {
        if (instance == null)
            instance = new ServiceManager();
        return instance;
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

    WorkflowInstance instantiateWorkflow(UUID workflowTypeId) {
        if (providedWorkflowTypes.containsKey(workflowTypeId)) {
            WorkflowInstance workflowInstance = new WorkflowInstance(providedWorkflowTypes.get(workflowTypeId));
            runningWorkflowInstances.put(workflowInstance.getId(), workflowInstance);
            return workflowInstance;
        } else
            throw new NoSuchElementException("Requested workflow " + workflowTypeId + " does not exist.");
    }

    public Map<UUID, WorkflowInstance> getRunningWorkflowInstances() {
        return runningWorkflowInstances;
    }
}
