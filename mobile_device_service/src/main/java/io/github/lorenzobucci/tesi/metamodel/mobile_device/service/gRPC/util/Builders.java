package io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.util;

import com.google.protobuf.util.Timestamps;
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
                .setEndpointURI(task.getEndpoint().toString())
                .setParameters(task.getParameters())
                .setAssociatedWorkflowId(task.getAssociatedWorkflowId())
                .build();
    }
}
