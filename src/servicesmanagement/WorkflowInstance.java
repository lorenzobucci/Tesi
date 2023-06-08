package servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;
import java.util.UUID;

public class WorkflowInstance {

    public final UUID id = UUID.randomUUID();

    public final UUID workflowTypeId;
    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG;

    public WorkflowInstance(WorkflowType workflowType) {
        workflowTypeId = workflowType.id;
        this.serviceInstanceDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType callerServiceType = workflowType.getDAG().getEdgeSource(edge);
            ServiceType calleeServiceType = workflowType.getDAG().getEdgeTarget(edge);

            ServiceInstance callerService = new ServiceInstance(callerServiceType);
            ServiceInstance calleeService = new ServiceInstance(calleeServiceType);

            serviceInstanceDAG.addVertex(callerService);
            serviceInstanceDAG.addVertex(calleeService);

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
