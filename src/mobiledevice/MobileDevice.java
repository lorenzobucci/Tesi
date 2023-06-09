package mobiledevice;

import servicesmanagement.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobileDevice {

    public final UUID id = UUID.randomUUID();

    private final Trajectory pastTrajectory = new Trajectory();

    private final ServiceGateway serviceGateway = ServiceGateway.getInstance();

    public Set<ServiceType> availableServiceTypes = serviceGateway.getAvailableServiceTypes(id);
    public Set<WorkflowType> availableWorkflowTypes = serviceGateway.getAvailableWorkflowTypes(id);
    public Set<ServiceInstance> ownedSingleServices = new HashSet<>();
    public Set<WorkflowInstance> ownedWorkflows = new HashSet<>();


    public MobileDevice() {

    }

    public void syncCurrentPosition(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }

    public void updateAvailableServiceTypes() {
        availableServiceTypes = serviceGateway.getAvailableServiceTypes(id);
    }

    public void updateAvailableWorkflowTypes() {
        availableWorkflowTypes = serviceGateway.getAvailableWorkflowTypes(id);
    }

    public void useService(ServiceType serviceType) {
        ownedSingleServices.add(serviceGateway.instantiateService(serviceType.id));
    }

    public void useWorkflow(WorkflowType workflowType) {
        ownedWorkflows.add(serviceGateway.instantiateWorkflow(workflowType.id));
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }


}
