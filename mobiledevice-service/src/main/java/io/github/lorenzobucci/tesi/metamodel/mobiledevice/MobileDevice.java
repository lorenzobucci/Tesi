package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import jakarta.persistence.*;

import java.net.URI;
import java.util.UUID;

@Entity(name = "mobile_device")
public class MobileDevice {

    @Id
    private UUID id = UUID.randomUUID();

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "past_trajectory_id")
    private Trajectory pastTrajectory = new Trajectory();

    @Transient // MAYBE NOT
    private TrajectoryForecaster trajectoryForecaster;

    public MobileDevice() {

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

    void syncWithRealObject(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }

    public UUID getId() {
        return id;
    }

    public void setTrajectoryForecaster(TrajectoryForecaster trajectoryForecaster) {
        this.trajectoryForecaster = trajectoryForecaster;
    }
}
