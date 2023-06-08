package servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;
import java.util.UUID;

public class WorkflowType {

    public final UUID id;

    private final DirectedAcyclicGraph<ServiceType, DefaultEdge> serviceTypeDAG;

    public WorkflowType() {
        serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);
        id = UUID.randomUUID();
    }

    public WorkflowType(WorkflowType workflowType) {
        id = workflowType.id;

        serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (DefaultEdge edge : workflowType.getDAG().edgeSet()) {
            ServiceType callerServiceType = new ServiceType(workflowType.getDAG().getEdgeSource(edge));
            ServiceType calleeServiceType = new ServiceType(workflowType.getDAG().getEdgeTarget(edge));

            serviceTypeDAG.addVertex(callerServiceType);
            serviceTypeDAG.addVertex(calleeServiceType);

            serviceTypeDAG.addEdge(callerServiceType, calleeServiceType);
        }
    }

    public void addRootService(ServiceType rootService) {
        try {
            serviceTypeDAG.addVertex(rootService);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The service already belongs to the workflow");
        }
    }

    public void addService(ServiceType newService, ServiceType caller) {
        boolean addResult = serviceTypeDAG.addVertex(newService);
        if (!addResult)
            throw new IllegalArgumentException("The service already belongs to the workflow");

        try {
            serviceTypeDAG.addEdge(caller, newService);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Caller service must belong to the workflow");
        }

    }

    public void addRPC(ServiceType callerService, ServiceType calleeService) {
        if (serviceTypeDAG.containsVertex(callerService)) {
            if (serviceTypeDAG.containsVertex(calleeService))
                serviceTypeDAG.addEdge(callerService, calleeService);
            else
                throw new IllegalArgumentException("Callee service must belong to the workflow");
        } else
            throw new IllegalArgumentException("Caller service must belong to the workflow");
    }

    public boolean contains(ServiceType service) {
        return serviceTypeDAG.containsVertex(service);
    }

    public void removeService(ServiceType service) {
        boolean removeResult = serviceTypeDAG.removeVertex(service);
        if (!removeResult)
            throw new IllegalArgumentException("The service does not belong to the workflow");

    }

    DirectedAcyclicGraph<ServiceType, DefaultEdge> getDAG() {
        return serviceTypeDAG;
    }

    Set<ServiceType> getServiceTypes() {
        return serviceTypeDAG.vertexSet();
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
}
