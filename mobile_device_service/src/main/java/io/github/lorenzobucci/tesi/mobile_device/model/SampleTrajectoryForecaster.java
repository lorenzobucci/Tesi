package io.github.lorenzobucci.tesi.mobile_device.model;

import java.sql.Timestamp;

public class SampleTrajectoryForecaster implements TrajectoryForecaster {
    @Override
    public Trajectory forecast(Trajectory pastTrajectory) {
        Trajectory forecastedTrajectory = new Trajectory(pastTrajectory);
        forecastedTrajectory.addPosition(new Position(41.23f, 12.14f, (new Timestamp(System.currentTimeMillis()))));
        return forecastedTrajectory;
    }
}
