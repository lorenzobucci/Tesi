package io.github.lorenzobucci.tesi.mobile_device.model;

public interface TrajectoryForecaster {
    Trajectory forecast(Trajectory pastTrajectory);
}
