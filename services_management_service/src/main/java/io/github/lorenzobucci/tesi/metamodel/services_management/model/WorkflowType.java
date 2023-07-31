package io.github.lorenzobucci.tesi.metamodel.services_management.model;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
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

    }

    public void addRPC(ServiceType callerService, ServiceType calleeService) throws NoSuchElementException {
        if (serviceTypeDAG.containsVertex(callerService)) {
            if (serviceTypeDAG.containsVertex(calleeService))
                serviceTypeDAG.addEdge(callerService, calleeService);
            else
                throw new NoSuchElementException("Callee service " + calleeService.getId() + " must belong to the workflow.");
        } else
            throw new NoSuchElementException("Caller service " + callerService.getId() + " must belong to the workflow.");
    }

    public boolean contains(ServiceType serviceType) {
        return serviceTypeDAG.vertexSet().contains(serviceType);
    }

    public void removeServiceType(ServiceType service) throws NoSuchElementException, IllegalArgumentException {
        if (!service.equals(endpointServiceType)) {
            boolean removeResult = serviceTypeDAG.removeVertex(service);
            if (!removeResult)
                throw new NoSuchElementException("The service " + service.getId() + " does not belong to the workflow.");
        } else
            throw new IllegalArgumentException("Cannot remove the endpoint service.");
    }

    public void updateEndpointServiceType(EndpointServiceType endpointServiceType) {
        if (!serviceTypeDAG.containsVertex(endpointServiceType)) {
            if (serviceTypeDAG.vertexSet().isEmpty()) {
                serviceTypeDAG.addVertex(endpointServiceType);
                this.endpointServiceType = endpointServiceType;
            } else {
                serviceTypeDAG.addVertex(endpointServiceType);
                for (ServiceType descendant : serviceTypeDAG.getDescendants(this.endpointServiceType))
                    serviceTypeDAG.addEdge(endpointServiceType, descendant);
                serviceTypeDAG.removeVertex(this.endpointServiceType);
                this.endpointServiceType = endpointServiceType;
            }
        } else
            throw new NoSuchElementException("The service " + endpointServiceType.getId() + " already belongs to the workflow.");
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

    @Access(AccessType.PROPERTY)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "workflow_types_graphs",
            joinColumns = @JoinColumn(name = "workflow_type_id", referencedColumnName = "id"))
    @AssociationOverrides({
            @AssociationOverride(name = "callerService", joinColumns = @JoinColumn(name = "caller_service_type_id")),
            @AssociationOverride(name = "calleeService", joinColumns = @JoinColumn(name = "callee_service_type_id"))
    })
    protected Set<ServiceTypeGraphEdge> getGraphEdges() {
        Set<ServiceTypeGraphEdge> set = new HashSet<>();
        for (DefaultEdge edge : serviceTypeDAG.edgeSet())
            set.add(new ServiceTypeGraphEdge(serviceTypeDAG.getEdgeSource(edge), serviceTypeDAG.getEdgeTarget(edge)));
        if (set.isEmpty() && endpointServiceType != null) // WORKFLOW WITH ONLY THE ENDPOINT
            set.add(new ServiceTypeGraphEdge(null, endpointServiceType));
        return set;
    }

    protected void setGraphEdges(Set<ServiceTypeGraphEdge> graphEdges) {
        for (ServiceTypeGraphEdge edge : graphEdges) {
            serviceTypeDAG.addVertex(edge.calleeService);
            if (edge.callerService != null) { // WORKFLOW WITH 2 OR MORE SERVICES
                serviceTypeDAG.addVertex(edge.callerService);
                serviceTypeDAG.addEdge(edge.callerService, edge.calleeService);
            }
        }
    }

    @Embeddable
    protected static class ServiceTypeGraphEdge {

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JoinColumn(name = "caller_service")
        private ServiceType callerService;

        @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
        @JoinColumn(name = "callee_service", nullable = false)
        private ServiceType calleeService;

        ServiceTypeGraphEdge(ServiceType callerService, ServiceType calleeService) {
            this.callerService = callerService;
            this.calleeService = calleeService;
        }

        protected ServiceTypeGraphEdge() {

        }
    }
}
