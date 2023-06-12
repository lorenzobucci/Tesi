package mobiledevice;

import servicesmanagement.ServiceGateway;
import servicesmanagement.WorkflowInstance;
import servicesmanagement.WorkflowType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobileDevice {

    public final UUID id = UUID.randomUUID();

    private final Trajectory pastTrajectory = new Trajectory();

    private final ServiceGateway serviceGateway = ServiceGateway.getInstance();

    public Set<WorkflowType> availableWorkflow = serviceGateway.getAvailableWorkflowTypes(id);
    public Set<WorkflowInstance> ownedWorkflows = new HashSet<>();


    public MobileDevice() {

    }

    public void syncCurrentPosition(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }

    public void updateAvailableWorkflow() {
        availableWorkflow = serviceGateway.getAvailableWorkflowTypes(id);
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
