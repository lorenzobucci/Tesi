package servicesmanagement;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;

public class WorkflowType {

    private final DirectedAcyclicGraph<ServiceType, DefaultEdge> serviceTypeDAG;

    public WorkflowType() {
        this.serviceTypeDAG = new DirectedAcyclicGraph<>(DefaultEdge.class);
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

}
