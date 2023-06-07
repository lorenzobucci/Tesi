package servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;

public class WorkflowInstance {

    private final WorkflowType workflowType;

    private final DirectedAcyclicGraph<ServiceInstance, DefaultEdge> serviceInstanceDAG;

    public WorkflowInstance(WorkflowType workflowType) {
        this.workflowType = workflowType;
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
}
