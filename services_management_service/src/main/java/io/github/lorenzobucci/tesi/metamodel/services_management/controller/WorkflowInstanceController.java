package io.github.lorenzobucci.tesi.metamodel.services_management.controller;

import io.github.lorenzobucci.tesi.metamodel.services_management.dao.WorkflowInstanceDao;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.WorkflowInstance;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.WorkflowRequirements;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.WorkflowType;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

public class WorkflowInstanceController {

    @Inject
    private WorkflowInstanceDao workflowInstanceDao;

    @Inject
    private WorkflowTypeController workflowTypeController;

    public WorkflowInstance instantiateWorkflowInstance(URI endpointURI, String endpointParameters, WorkflowRequirements workflowRequirements) throws NoSuchElementException {
        WorkflowType workflowType = workflowTypeController.getWorkflowType(endpointURI);

        WorkflowInstance workflowInstance = new WorkflowInstance(workflowType, endpointParameters, workflowRequirements);
        workflowInstanceDao.create(workflowInstance);
        return workflowInstanceDao.findByUuid(workflowInstance.getUuid());
    }

    public WorkflowInstance getWorkflowInstance(long workflowInstanceId) throws NoSuchElementException {
        WorkflowInstance workflowInstance = workflowInstanceDao.findById(workflowInstanceId);
        if (workflowInstance != null)
            return workflowInstance;
        else
            throw new NoSuchElementException("WorkflowInstance with id=" + workflowInstanceId + " does not exist.");
    }

    public List<WorkflowInstance> retrieveWorkflowInstances() {
        return workflowInstanceDao.findAll();
    }

    public void terminateWorkflowInstance(long workflowInstanceId) throws NoSuchElementException {
        WorkflowInstance workflowInstance = getWorkflowInstance(workflowInstanceId);
        workflowInstance.onCompleted();
        workflowInstanceDao.delete(workflowInstance);
    }

    public void updateWorkflowRequirements(long workflowInstanceId, WorkflowRequirements newWorkflowRequirements) throws NoSuchElementException {
        WorkflowInstance workflowInstance = getWorkflowInstance(workflowInstanceId);
        workflowInstance.updateWorkflowRequirements(newWorkflowRequirements);
        workflowInstanceDao.update(workflowInstance);
    }
}
