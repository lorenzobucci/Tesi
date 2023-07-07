package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "workflow_type")
public class WorkflowType {

    @Id
    private UUID id = UUID.randomUUID();

    @Transient // PERSISTED USING PROPERTY MODE
    private final DirectedAcyclicGraph<ServiceType, DefaultEdge> serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endpoint_service_type_id", nullable = false)
    private EndpointServiceType endpointServiceType;

    public WorkflowType() {

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

    public boolean contains(ServiceType serviceType) {
        return serviceTypeDAG.vertexSet().contains(serviceType);
    }

    public void removeServiceType(ServiceType service) {
        if (!service.equals(endpointServiceType)) {
            boolean removeResult = serviceTypeDAG.removeVertex(service);
            if (!removeResult)
                throw new NoSuchElementException("The service " + service.getId() + " does not belong to the workflow.");
        } else
            throw new IllegalArgumentException("Cannot remove the endpoint service.");
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

    public void setEndpointServiceType(EndpointServiceType endpointServiceType) {
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

    public UUID getId() {
        return id;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "workflow_types_graphs",
            joinColumns = @JoinColumn(name = "workflow_type_id", referencedColumnName = "id"))
    @AttributeOverrides({
            @AttributeOverride(name = "firstElement", column = @Column(name = "caller_service_type_id")),
            @AttributeOverride(name = "secondElement", column = @Column(name = "callee_service_type_id"))
    })
    protected Set<ServiceTypePair> getGraphEdges() {
        Set<ServiceTypePair> set = new HashSet<>();
        for (DefaultEdge edge : serviceTypeDAG.edgeSet())
            set.add(new ServiceTypePair(serviceTypeDAG.getEdgeSource(edge), serviceTypeDAG.getEdgeTarget(edge)));
        return set;
    }

    protected void setGraphEdges(Set<ServiceTypePair> graphEdges) {
        for (ServiceTypePair pair : graphEdges) {
            serviceTypeDAG.addVertex(pair.firstElement);
            serviceTypeDAG.addVertex(pair.secondElement);
            serviceTypeDAG.addEdge(pair.firstElement, pair.secondElement);
        }
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

    @Embeddable
    static
    class ServiceTypePair {

        @ManyToOne(optional = false)
        @JoinColumn(name = "first_element_id", nullable = false)
        private ServiceType firstElement;

        @ManyToOne(optional = false)
        @JoinColumn(name = "second_element_id", nullable = false)
        private ServiceType secondElement;

        ServiceTypePair(ServiceType firstElement, ServiceType secondElement) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
        }

        protected ServiceTypePair() {

        }
    }
}
