package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class WorkflowInstance {

    private final UUID id = UUID.randomUUID();

    private final UUID workflowTypeId;

    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG;

    WorkflowInstance(WorkflowType workflowType) {
        workflowTypeId = workflowType.getId();
        serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType callerServiceType = workflowType.getDAG().getEdgeSource(edge);
            ServiceType calleeServiceType = workflowType.getDAG().getEdgeTarget(edge);

            ServiceInstance callerService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.getServiceType().equals(callerServiceType)).findAny().orElse(null);
            if (callerService == null) {
                callerService = new ServiceInstance(callerServiceType, this);
                serviceInstanceDAG.addVertex(callerService);
            }

            ServiceInstance calleeService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.getServiceType().equals(calleeServiceType)).findAny().orElse(null);
            if (calleeService == null) {
                calleeService = new ServiceInstance(calleeServiceType, this);
                serviceInstanceDAG.addVertex(calleeService);
            }

            serviceInstanceDAG.addEdge(callerService, calleeService);
        }
    }

    DirectedAcyclicGraph<ServiceInstance, DefaultEdge> getDAG() {
        return serviceInstanceDAG;
    }

    public Iterator<ServiceInstance> getServicesIterator() {
        return serviceInstanceDAG.iterator();
    }

    public ServiceInstance getRootService() {
        return serviceInstanceDAG.iterator().next();
    }

    public Set<ServiceInstance> getServiceInstances() {
        return serviceInstanceDAG.vertexSet();
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkflowTypeId() {
        return workflowTypeId;
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
}
