package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

public interface TrajectoryForecaster {
    Trajectory forecast(Trajectory pastTrajectory);
}
