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

    public void addRootServiceType(ServiceType rootService) {
        if (serviceTypeDAG.vertexSet().isEmpty()) {
            try {
                serviceTypeDAG.addVertex(rootService);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The service " + rootService.id + " already belongs to the workflow");
            }
        } else {
            throw new IllegalStateException("The root service has already been added");
        }
    }

    public void addServiceType(ServiceType newService, ServiceType callerService) {
        if (serviceTypeDAG.containsVertex(callerService)) {
            boolean addResult = serviceTypeDAG.addVertex(newService);
            if (!addResult)
                throw new IllegalArgumentException("The service " + newService.id + " already belongs to the workflow");
            serviceTypeDAG.addEdge(callerService, newService);
        } else {
            throw new IllegalArgumentException("Caller service " + callerService.id + " must belong to the workflow");
        }

    }

    public void addRPC(ServiceType callerService, ServiceType calleeService) {
        if (serviceTypeDAG.containsVertex(callerService)) {
            if (serviceTypeDAG.containsVertex(calleeService))
                serviceTypeDAG.addEdge(callerService, calleeService);
            else
                throw new IllegalArgumentException("Callee service " + calleeService.id + " must belong to the workflow");
        } else
            throw new IllegalArgumentException("Caller service " + callerService.id + " must belong to the workflow");
    }

    public boolean contains(UUID serviceTypeId) {
        return serviceTypeDAG.vertexSet().stream().anyMatch(serviceType -> serviceType.id == serviceTypeId);
    }

    public void removeServiceType(ServiceType service) {
        boolean removeResult = serviceTypeDAG.removeVertex(service);
        if (!removeResult)
            throw new IllegalArgumentException("The service " + service.id + " does not belong to the workflow");

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
