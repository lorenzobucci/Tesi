package io.github.lorenzobucci.metamodel.mobiledevice;

public interface TrajectoryForecaster {
    Trajectory forecast(Trajectory pastTrajectory);
}
