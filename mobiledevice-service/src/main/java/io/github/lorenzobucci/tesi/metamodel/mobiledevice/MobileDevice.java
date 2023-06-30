package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import org.json.JSONObject;

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

    public URI useService(UUID serviceId, JSONObject parameters) {
        DependabilityRequirements requirements = new DependabilityRequirements(); // CALCULATE REQUIREMENTS BASED ON TRAJECTORY AND OTHER...
        return serviceProxy.requestService(serviceId, parameters, requirements); // TODO: USE API
    }

    public URI optimizeService(UUID serviceId) {
        Trajectory forecastedTrajectory = trajectoryForecaster.forecast(getPastTrajectory());
        DependabilityRequirements newDependabilityRequirements = new DependabilityRequirements();  // DO STUFF TO DETERMINE THE NEW REQUIREMENTS
        return serviceProxy.updateServiceRequirements(serviceId, newDependabilityRequirements); // TODO: USE API
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }


}
