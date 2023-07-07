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

    public WorkflowType() {

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
