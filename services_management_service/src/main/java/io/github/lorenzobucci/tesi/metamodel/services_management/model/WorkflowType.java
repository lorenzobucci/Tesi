package io.github.lorenzobucci.tesi.metamodel.services_management.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Table(name = "workflow_type")
public class WorkflowType extends BaseEntity {

    @Transient // PERSISTED USING PROPERTY MODE
    private final DirectedAcyclicGraph<ServiceType, DefaultEdge> serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private EndpointServiceType endpointServiceType;

    public WorkflowType(EndpointServiceType endpointServiceType) {
        serviceTypeDAG.addVertex(endpointServiceType);
        this.endpointServiceType = endpointServiceType;
    }

    protected WorkflowType() {

    }

    public void addServiceType(ServiceType newService, ServiceType callerService) throws IllegalArgumentException, NoSuchElementException {
        if (serviceTypeDAG.containsVertex(callerService)) {
            boolean addResult = serviceTypeDAG.addVertex(newService);
            if (!addResult)
                throw new IllegalArgumentException("The service " + newService.getId() + " already belongs to the workflow.");
            serviceTypeDAG.addEdge(callerService, newService);
        } else {
            throw new NoSuchElementException("Caller service " + callerService.getId() + " must belong to the workflow.");
        }
        storeGraphEdges();
    }

    public boolean contains(ServiceType serviceType) {
        return serviceTypeDAG.vertexSet().contains(serviceType);
    }

    public void addRPC(ServiceType callerService, ServiceType calleeService) throws NoSuchElementException {
        if (serviceTypeDAG.containsVertex(callerService)) {
            if (serviceTypeDAG.containsVertex(calleeService))
                serviceTypeDAG.addEdge(callerService, calleeService);
            else
                throw new NoSuchElementException("Callee service " + calleeService.getId() + " must belong to the workflow.");
        } else
            throw new NoSuchElementException("Caller service " + callerService.getId() + " must belong to the workflow.");
        storeGraphEdges();
    }

    public void removeServiceType(ServiceType service) throws NoSuchElementException, IllegalArgumentException {
        if (!service.equals(endpointServiceType)) {
            boolean removeResult = serviceTypeDAG.removeVertex(service);
            if (!removeResult)
                throw new NoSuchElementException("The service " + service.getId() + " does not belong to the workflow.");
        } else
            throw new IllegalArgumentException("Cannot remove the endpoint service.");
        storeGraphEdges();
    }

    public EndpointServiceType getEndpointServiceType() {
        return endpointServiceType;
    }

    DirectedAcyclicGraph<ServiceType, DefaultEdge> getDAG() {
        return serviceTypeDAG;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypeDAG.vertexSet();
    }

    public void updateEndpointServiceType(EndpointServiceType endpointServiceType) {
        if (!serviceTypeDAG.containsVertex(endpointServiceType)) {
            serviceTypeDAG.addVertex(endpointServiceType);
            for (ServiceType descendant : serviceTypeDAG.getDescendants(this.endpointServiceType))
                serviceTypeDAG.addEdge(endpointServiceType, descendant);
            serviceTypeDAG.removeVertex(this.endpointServiceType);
            this.endpointServiceType = endpointServiceType;
        } else
            throw new NoSuchElementException("The service " + endpointServiceType.getId() + " already belongs to the workflow.");
        storeGraphEdges();
    }

    // GRAPH PERSISTENCE SECTION

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "workflowType", orphanRemoval = true)
    private Set<ServiceTypeGraphEdge> graphEdges = new HashSet<>();

    private void storeGraphEdges() {
        graphEdges.clear();
        for (DefaultEdge edge : serviceTypeDAG.edgeSet())
            graphEdges.add(new ServiceTypeGraphEdge(serviceTypeDAG.getEdgeSource(edge), serviceTypeDAG.getEdgeTarget(edge), this));
    }

    @PostConstruct
    @PostLoad
    private void retrieveGraphEdges() {
        if (graphEdges.isEmpty())
            serviceTypeDAG.addVertex(endpointServiceType);
        else {
            for (ServiceTypeGraphEdge edge : graphEdges) {
                edge.workflowType = null; // NECESSARY TO DELETE THE EDGE FROM DB
                serviceTypeDAG.addVertex(edge.calleeService);
                serviceTypeDAG.addVertex(edge.callerService);
                serviceTypeDAG.addEdge(edge.callerService, edge.calleeService);
            }
        }
    }

    @Entity
    @Table(name = "service_type_graph_edge")
    protected static class ServiceTypeGraphEdge extends BaseEntity {

        @ManyToOne(optional = false)
        @JoinColumn(name = "caller_service", nullable = false)
        private ServiceType callerService;

        @ManyToOne(optional = false)
        @JoinColumn(name = "callee_service", nullable = false)
        private ServiceType calleeService;

        @ManyToOne(optional = false)
        @JoinColumn(name = "workflow_type_id", nullable = false)
        private WorkflowType workflowType;

        private ServiceTypeGraphEdge(ServiceType callerService, ServiceType calleeService, WorkflowType workflowType) {
            this.callerService = callerService;
            this.calleeService = calleeService;
            this.workflowType = workflowType;
        }

        protected ServiceTypeGraphEdge() {

        }
    }
}
