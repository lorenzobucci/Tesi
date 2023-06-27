package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

public interface TrajectoryForecaster {
    Trajectory forecast(Trajectory pastTrajectory);
}
