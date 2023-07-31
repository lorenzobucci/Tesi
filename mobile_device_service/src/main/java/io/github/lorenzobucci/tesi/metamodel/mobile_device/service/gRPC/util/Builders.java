package io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.util;

import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.DependabilityRequirements;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Position;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Task;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.MobileDevice;

public class Builders {

    private Builders() {
    }

    public static MobileDevice.MobileDeviceDTDTO buildMobileDeviceDTDTO(MobileDeviceDT mobileDeviceDT) {
        MobileDevice.MobileDeviceDTDTO.Builder mobileDeviceDTDTO = MobileDevice.MobileDeviceDTDTO.newBuilder().setId(mobileDeviceDT.getId());

        for (Task task : mobileDeviceDT.getRunningTasks())
            mobileDeviceDTDTO.addRunningTasksId(task.getId());

        for (Position position : mobileDeviceDT.getPastTrajectory().getPositionsSet())
            mobileDeviceDTDTO.addTrajectory(buildPositionDTO(position));

        mobileDeviceDTDTO.setTrajectoryForecaster(mobileDeviceDT.getTrajectoryForecaster().getClass().getName());

        return mobileDeviceDTDTO.build();
    }

    public static MobileDevice.PositionDTO buildPositionDTO(Position position) {
        return MobileDevice.PositionDTO.newBuilder()
                .setTimestamp(Timestamps.fromMillis(position.getTimestamp().getTime()))
                .setLatitude(position.getLatitude())
                .setLongitude(position.getLongitude())
                .build();
    }

    public static MobileDevice.TaskDTO buildTaskDTO(Task task) {
        return MobileDevice.TaskDTO.newBuilder().setId(task.getId())
                .setEndpointURI(task.getEndpointURI())
                .setParameters(task.getParameters())
                .setAssociatedWorkflowId(task.getAssociatedWorkflowId())
                .setDependabilityRequirements(buildDependabilityRequirementsDTO(task.getRequirements()))
                .build();
    }

    public static MobileDevice.DependabilityRequirementsDTO buildDependabilityRequirementsDTO(DependabilityRequirements dependabilityRequirements) {
        return MobileDevice.DependabilityRequirementsDTO.newBuilder().build(); // DO CONVERSION TO DTO
    }
}
