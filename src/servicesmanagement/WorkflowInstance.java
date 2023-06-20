package servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Iterator;
import java.util.UUID;

public class WorkflowInstance {

    public final UUID id = UUID.randomUUID();

    public final UUID workflowTypeId;

    private UserRequirements userRequirements;
    final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG;

    WorkflowInstance(WorkflowType workflowType) {
        workflowTypeId = workflowType.id;
        serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType callerServiceType = workflowType.getDAG().getEdgeSource(edge);
            ServiceType calleeServiceType = workflowType.getDAG().getEdgeTarget(edge);

            ServiceInstance callerService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.serviceType.id == callerServiceType.id).findAny().orElse(null);
            if (callerService == null) {
                callerService = new ServiceInstance(callerServiceType, this);
                serviceInstanceDAG.addVertex(callerService);
            }

            ServiceInstance calleeService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.serviceType.id == calleeServiceType.id).findAny().orElse(null);
            if (calleeService == null) {
                calleeService = new ServiceInstance(calleeServiceType, this);
                serviceInstanceDAG.addVertex(calleeService);
            }

            serviceInstanceDAG.addEdge(callerService, calleeService);
        }
    }

    public UserRequirements getUserRequirements() {
        return userRequirements;
    }

    public Iterator<ServiceInstance> getServicesIterator() {
        return serviceInstanceDAG.iterator();
    }

    void setUserRequirements(UserRequirements userRequirements) {
        this.userRequirements = userRequirements;
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
