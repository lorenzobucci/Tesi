package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.controller;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao.WorkflowTypeDao;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.WorkflowType;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class WorkflowTypeController {

    @Inject
    private WorkflowTypeDao workflowTypeDao;

    @Inject
    private EndpointServiceTypeController endpointServiceTypeController;

    @Inject
    private ServiceTypeController serviceTypeController;

    public WorkflowType addWorkflowType(long endpointServiceTypeId) throws NoSuchElementException {
        WorkflowType workflowType = new WorkflowType(endpointServiceTypeController.getEndpointServiceType(endpointServiceTypeId));
        workflowTypeDao.create(workflowType);
        return workflowTypeDao.findByUuid(workflowType.getUuid());
    }

    public WorkflowType getWorkflowType(long workflowTypeId) throws NoSuchElementException {
        WorkflowType workflowType = workflowTypeDao.findById(workflowTypeId);
        if (workflowType != null)
            return workflowType;
        else
            throw new NoSuchElementException("WorkflowType with id=" + workflowTypeId + " does not exist.");
    }

    public WorkflowType getWorkflowType(URI endpointURI) throws NoSuchElementException {
        WorkflowType workflowType = workflowTypeDao.findByEndpoint(endpointServiceTypeController.getEndpointServiceType(endpointURI));
        if (workflowType != null)
            return workflowType;
        else
            throw new NoSuchElementException("WorkflowType with endpoint identified by URI=" + endpointURI + " does not exist.");
    }

    public List<WorkflowType> retrieveWorkflowTypes() {
        return workflowTypeDao.findAll();
    }

    public void removeWorkflowType(long workflowTypeId, boolean removeEndpointServiceType) throws NoSuchElementException, ConstraintViolationException {
        WorkflowType workflowType = getWorkflowType(workflowTypeId);
        long endpointServiceTypeId = workflowType.getEndpointServiceType().getId();

        workflowTypeDao.delete(workflowType);

        if (removeEndpointServiceType)
            endpointServiceTypeController.removeEndpointServiceType(endpointServiceTypeId);
    }

    public void addServiceTypeToWorkflow(long workflowTypeId, long serviceTypeToAddId, long callerServiceTypeId) throws NoSuchElementException, IllegalArgumentException {
        WorkflowType workflowType = getWorkflowType(workflowTypeId);
        workflowType.addServiceType(serviceTypeController.getServiceType(serviceTypeToAddId), serviceTypeController.getServiceType(callerServiceTypeId));
        workflowTypeDao.update(workflowType);
    }

    public void addRPCToWorkflow(long workflowTypeId, long callerServiceTypeId, long calleeServiceTypeId) throws NoSuchElementException {
        WorkflowType workflowType = getWorkflowType(workflowTypeId);
        workflowType.addRPC(serviceTypeController.getServiceType(callerServiceTypeId), serviceTypeController.getServiceType(calleeServiceTypeId));
        workflowTypeDao.update(workflowType);
    }

    public boolean workflowTypeContainsServiceType(long workflowTypeId, long serviceTypeId) throws NoSuchElementException {
        return getWorkflowType(workflowTypeId).contains(serviceTypeController.getServiceType(serviceTypeId));
    }

    public void removeServiceTypeFromWorkflow(long workflowTypeId, long serviceTypeId) throws NoSuchElementException, IllegalArgumentException {
        WorkflowType workflowType = getWorkflowType(workflowTypeId);
        workflowType.removeServiceType(serviceTypeController.getServiceType(serviceTypeId));
        workflowTypeDao.update(workflowType);
    }

    public void updateWorkflowEndpointServiceType(long workflowTypeId, long endpointServiceTypeId) throws NoSuchElementException {
        WorkflowType workflowType = getWorkflowType(workflowTypeId);
        workflowType.updateEndpointServiceType(endpointServiceTypeController.getEndpointServiceType(endpointServiceTypeId));
        workflowTypeDao.update(workflowType);
    }

    public Set<ServiceType> retrieveServiceTypesFromWorkflow(long workflowTypeId) throws NoSuchElementException {
        return getWorkflowType(workflowTypeId).getServiceTypes();
    }
}
