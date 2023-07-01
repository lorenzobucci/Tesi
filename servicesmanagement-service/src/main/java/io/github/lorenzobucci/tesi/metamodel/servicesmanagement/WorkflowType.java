package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public class WorkflowType {

    private final UUID id;

    private final DirectedAcyclicGraph<ServiceType, DefaultEdge> serviceTypeDAG;

    public WorkflowType() {
        serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);
        id = UUID.randomUUID();
    }

    public void addRootServiceType(ServiceType rootService) {
        if (serviceTypeDAG.vertexSet().isEmpty()) {
            boolean addResult = serviceTypeDAG.addVertex(rootService);
            if (!addResult)
                throw new IllegalArgumentException("The service " + rootService.getId() + " already belongs to the workflow.");

        } else {
            throw new IllegalStateException("The root service has already been added.");
        }
    }

    public void addServiceType(ServiceType newService, ServiceType callerService) {
        if (serviceTypeDAG.containsVertex(callerService)) {
            boolean addResult = serviceTypeDAG.addVertex(newService);
            if (!addResult)
                throw new IllegalArgumentException("The service " + newService.getId() + " already belongs to the workflow.");
            serviceTypeDAG.addEdge(callerService, newService);
        } else {
            throw new NoSuchElementException("Caller service " + callerService.getId() + " must belong to the workflow.");
        }

    }

    public void addRPC(ServiceType callerService, ServiceType calleeService) {
        if (serviceTypeDAG.containsVertex(callerService)) {
            if (serviceTypeDAG.containsVertex(calleeService))
                serviceTypeDAG.addEdge(callerService, calleeService);
            else
                throw new NoSuchElementException("Callee service " + calleeService.getId() + " must belong to the workflow.");
        } else
            throw new NoSuchElementException("Caller service " + callerService.getId() + " must belong to the workflow.");
    }

    public boolean contains(UUID serviceTypeId) {
        return serviceTypeDAG.vertexSet().stream().anyMatch(serviceType -> serviceType.getId().equals(serviceTypeId));
    }

    public void removeServiceType(ServiceType service) {
        boolean removeResult = serviceTypeDAG.removeVertex(service);
        if (!removeResult)
            throw new NoSuchElementException("The service " + service.getId() + " does not belong to the workflow.");

    }

    DirectedAcyclicGraph<ServiceType, DefaultEdge> getDAG() {
        return serviceTypeDAG;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypeDAG.vertexSet();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowType that = (WorkflowType) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
