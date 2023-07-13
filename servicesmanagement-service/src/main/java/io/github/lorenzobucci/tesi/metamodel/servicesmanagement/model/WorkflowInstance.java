package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "workflow_instance")
public class WorkflowInstance extends BaseEntity {

    @Transient // PERSISTED USING PROPERTY MODE
    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endpoint_service_instance_id", nullable = false, unique = true)
    private EndpointServiceInstance endpointServiceInstance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_type_id", nullable = false)
    private WorkflowType workflowType;

    @Embedded
    private WorkflowRequirements workflowRequirements;

    public WorkflowInstance(WorkflowType workflowType, String endpointParameters, WorkflowRequirements workflowRequirements) {
        this.workflowType = workflowType;
        this.workflowRequirements = workflowRequirements;

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

    public void updateWorkflowRequirements(WorkflowRequirements workflowRequirements) {
        this.workflowRequirements = workflowRequirements;
        for (ServiceInstance serviceInstance : serviceInstanceDAG)
            serviceInstance.optimize();
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

    public WorkflowRequirements getWorkflowRequirements() {
        return workflowRequirements;
    }

    public WorkflowType getWorkflowType() {
        return workflowType;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "workflow_instance_id", referencedColumnName = "id", nullable = false)
    protected Set<ServiceInstanceGraphEdge> getGraphEdges() {
        Set<ServiceInstanceGraphEdge> set = new HashSet<>();
        for (DefaultEdge edge : serviceInstanceDAG.edgeSet())
            set.add(new ServiceInstanceGraphEdge(serviceInstanceDAG.getEdgeSource(edge), serviceInstanceDAG.getEdgeTarget(edge)));
        if (set.isEmpty() && endpointServiceInstance != null) // WORKFLOW WITH ONLY THE ENDPOINT
            set.add(new ServiceInstanceGraphEdge(null, endpointServiceInstance));
        return set;
    }

    protected void setGraphEdges(Set<ServiceInstanceGraphEdge> graphEdges) {
        for (ServiceInstanceGraphEdge edge : graphEdges) {
            serviceInstanceDAG.addVertex(edge.calleeService);
            if (edge.callerService != null) { // WORKFLOW WITH 2 OR MORE SERVICES
                serviceInstanceDAG.addVertex(edge.callerService);
                serviceInstanceDAG.addEdge(edge.callerService, edge.calleeService);
            }
        }
    }

    @Entity
    @Table(name = "service_instance_graph_edge")
    static
    class ServiceInstanceGraphEdge extends BaseEntity {

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "caller_service")
        private ServiceInstance callerService;

        @ManyToOne(cascade = CascadeType.ALL, optional = false)
        @JoinColumn(name = "callee_service", nullable = false)
        private ServiceInstance calleeService;

        ServiceInstanceGraphEdge(ServiceInstance callerService, ServiceInstance calleeService) {
            this.callerService = callerService;
            this.calleeService = calleeService;
        }

        protected ServiceInstanceGraphEdge() {

        }
    }
}
