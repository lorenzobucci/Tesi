package servicesmanagement;

import mobiledevice.UserRequirements;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;
import java.util.UUID;

public class WorkflowInstance {

    public final UUID id = UUID.randomUUID();

    public final UUID workflowTypeId;
    public UserRequirements userRequirements;
    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG;

    WorkflowInstance(WorkflowType workflowType, UserRequirements userRequirements) {
        workflowTypeId = workflowType.id;
        this.userRequirements = userRequirements;
        serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType callerServiceType = workflowType.getDAG().getEdgeSource(edge);
            ServiceType calleeServiceType = workflowType.getDAG().getEdgeTarget(edge);

            ServiceInstance callerService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.serviceType.id == callerServiceType.id).findAny().orElse(null);
            if (callerService == null) {
                callerService = new ServiceInstance(callerServiceType);
                serviceInstanceDAG.addVertex(callerService);
            }

            ServiceInstance calleeService = serviceInstanceDAG.vertexSet().stream().filter(serviceInstance -> serviceInstance.serviceType.id == calleeServiceType.id).findAny().orElse(null);
            if (calleeService == null) {
                calleeService = new ServiceInstance(calleeServiceType);
                serviceInstanceDAG.addVertex(calleeService);
            }

            serviceInstanceDAG.addEdge(callerService, calleeService);
        }
    }

    Set<ServiceInstance> getServiceInstances() {
        return serviceInstanceDAG.vertexSet();
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
