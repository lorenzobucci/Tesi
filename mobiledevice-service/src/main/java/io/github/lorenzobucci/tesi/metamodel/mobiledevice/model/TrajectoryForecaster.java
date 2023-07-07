package io.github.lorenzobucci.tesi.metamodel.mobiledevice.model;

public interface TrajectoryForecaster {
    Trajectory forecast(Trajectory pastTrajectory);
}
