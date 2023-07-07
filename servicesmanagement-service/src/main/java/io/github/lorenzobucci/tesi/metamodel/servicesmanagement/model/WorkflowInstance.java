package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "workflow_instance")
public class WorkflowInstance {

    @Id
    private UUID id = UUID.randomUUID();

    @Transient // PERSISTED USING PROPERTY MODE
    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endpoint_service_instance_id", nullable = false)
    private EndpointServiceInstance endpointServiceInstance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_type_id", nullable = false)
    private WorkflowType workflowType;

    WorkflowInstance(WorkflowType workflowType, String endpointParameters) {
        this.workflowType = workflowType;

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType calleeServiceType = workflowType.getDAG().getEdgeTarget(edge);

            ServiceInstance calleeService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.getServiceType().equals(calleeServiceType)).findAny().orElse(null);
            if (calleeService == null) {
                calleeService = new ServiceInstance(calleeServiceType, this);
                serviceInstanceDAG.addVertex(calleeService);
            }

            ServiceType callerServiceType = workflowType.getDAG().getEdgeSource(edge);

            if (!callerServiceType.equals(workflowType.getEndpointServiceType())) {
                ServiceInstance callerService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.getServiceType().equals(callerServiceType)).findAny().orElse(null);
                if (callerService == null) {
                    callerService = new ServiceInstance(callerServiceType, this);
                    serviceInstanceDAG.addVertex(callerService);
                }
                serviceInstanceDAG.addEdge(callerService, calleeService);
            } else {
                if (endpointServiceInstance == null) {
                    endpointServiceInstance = new EndpointServiceInstance(workflowType.getEndpointServiceType(), this, endpointParameters);
                    serviceInstanceDAG.addVertex(endpointServiceInstance);
                }
                serviceInstanceDAG.addEdge(endpointServiceInstance, calleeService);
            }
        }
    }

    protected WorkflowInstance() {

    }

    DirectedAcyclicGraph<ServiceInstance, DefaultEdge> getDAG() {
        return serviceInstanceDAG;
    }

    public Iterator<ServiceInstance> getServicesIterator() {
        return serviceInstanceDAG.iterator();
    }

    public Set<ServiceInstance> getServiceInstances() {
        return serviceInstanceDAG.vertexSet();
    }

    public EndpointServiceInstance getEndpointServiceInstance() {
        return endpointServiceInstance;
    }

    public UUID getId() {
        return id;
    }

    public WorkflowType getWorkflowType() {
        return workflowType;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "workflow_instances_graphs",
            joinColumns = @JoinColumn(name = "workflow_instance_id", referencedColumnName = "id"))
    @AttributeOverrides({
            @AttributeOverride(name = "firstElement", column = @Column(name = "caller_service_instance_id")),
            @AttributeOverride(name = "secondElement", column = @Column(name = "callee_service_instance_id"))
    })
    protected Set<ServiceInstancePair> getGraphEdges() {
        Set<ServiceInstancePair> set = new HashSet<>();
        for (DefaultEdge edge : serviceInstanceDAG.edgeSet())
            set.add(new ServiceInstancePair(serviceInstanceDAG.getEdgeSource(edge), serviceInstanceDAG.getEdgeTarget(edge)));
        return set;
    }

    protected void setGraphEdges(Set<ServiceInstancePair> graphEdges) {
        for (ServiceInstancePair pair : graphEdges) {
            serviceInstanceDAG.addVertex(pair.firstElement);
            serviceInstanceDAG.addVertex(pair.secondElement);
            serviceInstanceDAG.addEdge(pair.firstElement, pair.secondElement);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowInstance that = (WorkflowInstance) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Embeddable
    static
    class ServiceInstancePair {

        @ManyToOne(cascade = CascadeType.ALL, optional = false)
        @JoinColumn(name = "first_element_id", nullable = false)
        private ServiceInstance firstElement;

        @ManyToOne(cascade = CascadeType.ALL, optional = false)
        @JoinColumn(name = "second_element_id", nullable = false)
        private ServiceInstance secondElement;

        ServiceInstancePair(ServiceInstance firstElement, ServiceInstance secondElement) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
        }

        protected ServiceInstancePair() {

        }
    }
}
