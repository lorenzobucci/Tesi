package io.github.lorenzobucci.metamodel.mobiledevice;

import io.github.lorenzobucci.metamodel.resourcesmanagement.UserRequirements;
import io.github.lorenzobucci.metamodel.servicesmanagement.ServiceGateway;
import io.github.lorenzobucci.metamodel.servicesmanagement.WorkflowInstance;
import io.github.lorenzobucci.metamodel.servicesmanagement.WorkflowType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobileDevice {

    public final UUID id = UUID.randomUUID();

    private final Trajectory pastTrajectory = new Trajectory();
    public TrajectoryForecaster trajectoryForecaster;

    private final ServiceGateway serviceGateway = ServiceGateway.getInstance();  // TODO: USE API

    public Set<WorkflowType> availableWorkflows = serviceGateway.getAvailableWorkflowTypes(id); // TODO: USE API
    public final Set<WorkflowInstance> ownedWorkflows = new HashSet<>();


    public MobileDevice() {

    }

    public void syncWithRealObject(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }

    public void updateAvailableWorkflows() {
        availableWorkflows = serviceGateway.getAvailableWorkflowTypes(id); // TODO: USE API
    }

    public void useWorkflow(WorkflowType workflowType, UserRequirements requirements) {
        ownedWorkflows.add(serviceGateway.instantiateWorkflow(workflowType.id, requirements)); // TODO: USE API
    }

    public void reviseWorkflowBasedOnTrajectory(WorkflowInstance workflowInstance) {
        Trajectory forecastedTrajectory = trajectoryForecaster.forecast(getPastTrajectory());
        UserRequirements newUserRequirements = new UserRequirements();  // DO STUFF TO DETERMINE THE NEW REQUIREMENTS
        serviceGateway.updateWorkflowRequirements(workflowInstance.id, newUserRequirements); // TODO: USE API
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }


}
