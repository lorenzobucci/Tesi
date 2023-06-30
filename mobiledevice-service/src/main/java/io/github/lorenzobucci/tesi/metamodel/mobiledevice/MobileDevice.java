package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import java.net.URI;
import java.util.UUID;

public class MobileDevice {

    public final UUID id = UUID.randomUUID();

    private final Trajectory pastTrajectory = new Trajectory();
    public TrajectoryForecaster trajectoryForecaster;

    public MobileDevice() {

    }

    public void syncWithRealObject(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }

    public URI useService(UUID serviceId, String parameters) {
        DependabilityRequirements requirements = new DependabilityRequirements(); // CALCULATE REQUIREMENTS BASED ON TRAJECTORY AND OTHER...
        return ServiceProxy.getInstance().requestService(serviceId, parameters, requirements.toString()); // TODO: USE API
    }

    public URI optimizeService(UUID serviceId) {
        Trajectory forecastedTrajectory = trajectoryForecaster.forecast(getPastTrajectory());
        DependabilityRequirements newDependabilityRequirements = new DependabilityRequirements();  // DO STUFF TO DETERMINE THE NEW REQUIREMENTS
        return ServiceProxy.getInstance().updateServiceRequirements(serviceId, newDependabilityRequirements.toString()); // TODO: USE API
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }


}
