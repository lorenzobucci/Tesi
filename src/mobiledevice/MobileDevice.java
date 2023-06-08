package mobiledevice;

import java.util.UUID;

public class MobileDevice {

    private Trajectory pastTrajectory;
    private UUID id;

    public MobileDevice() {
        id = UUID.randomUUID();
        pastTrajectory = new Trajectory();
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getPositionsSet().last();
    }

    public void updateCurrentPosition(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
    }


}
