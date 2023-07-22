package io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.controller.MobileDeviceDTController;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.controller.TaskController;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Position;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Task;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.util.List;

public class CrudService extends CrudGrpc.CrudImplBase {

    @Inject
    private MobileDeviceDTController mobileDeviceDTController;

    @Inject
    private TaskController taskController;

    @Override
    public void addMobileDeviceDT(MobileDevice.MobileDeviceDTConstructorParameters request, StreamObserver<MobileDevice.MobileDeviceDTDTO> responseObserver) {
        MobileDeviceDT mobileDeviceDT = mobileDeviceDTController.addMobileDeviceDT();
        responseObserver.onNext(MobileDevice.MobileDeviceDTDTO.newBuilder().setId(mobileDeviceDT.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMobileDeviceDT(MobileDevice.MobileDeviceDTDTO request, StreamObserver<MobileDevice.MobileDeviceDTDTO> responseObserver) {
        try {
            MobileDeviceDT mobileDeviceDT = mobileDeviceDTController.getMobileDeviceDT(request.getId());
            responseObserver.onNext(buildMobileDeviceDTDTO(mobileDeviceDT));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveMobileDeviceDTs(Empty request, StreamObserver<MobileDevice.MobileDeviceDTList> responseObserver) {
        List<MobileDeviceDT> mobileDeviceDTs = mobileDeviceDTController.retrieveMobileDeviceDTs();
        MobileDevice.MobileDeviceDTList.Builder mobileDeviceDTList = MobileDevice.MobileDeviceDTList.newBuilder();

        for (MobileDeviceDT mobileDeviceDT : mobileDeviceDTs)
            mobileDeviceDTList.addMobileDeviceDTs(buildMobileDeviceDTDTO(mobileDeviceDT));

        responseObserver.onNext(mobileDeviceDTList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeMobileDeviceDT(MobileDevice.MobileDeviceDTDTO request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.removeMobileDeviceDT(request.getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getTask(MobileDevice.TaskDTO request, StreamObserver<MobileDevice.TaskDTO> responseObserver) {
        try {
            Task task = taskController.getTask(request.getId());
            responseObserver.onNext(buildTaskDTO(task));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveTasks(Empty request, StreamObserver<MobileDevice.TaskList> responseObserver) {
        List<Task> tasks = taskController.retrieveTasks();
        MobileDevice.TaskList.Builder taskList = MobileDevice.TaskList.newBuilder();

        for (Task task : tasks)
            taskList.addTasks(buildTaskDTO(task));

        responseObserver.onNext(taskList.build());
        responseObserver.onCompleted();
    }

    private MobileDevice.MobileDeviceDTDTO buildMobileDeviceDTDTO(MobileDeviceDT mobileDeviceDT) {
        MobileDevice.MobileDeviceDTDTO.Builder mobileDeviceDTDTO = MobileDevice.MobileDeviceDTDTO.newBuilder().setId(mobileDeviceDT.getId());

        for (Task task : mobileDeviceDT.getRunningTasks())
            mobileDeviceDTDTO.addRunningTasks(buildTaskDTO(task));

        for (Position position : mobileDeviceDT.getPastTrajectory().getPositionsSet())
            mobileDeviceDTDTO.addTrajectory(buildPositionDTO(position));

        return mobileDeviceDTDTO.build();
    }

    private MobileDevice.PositionDTO buildPositionDTO(Position position) {
        return MobileDevice.PositionDTO.newBuilder()
                .setTimestamp(Timestamps.fromMillis(position.getTimestamp().getTime()))
                .setLatitude(position.getLatitude())
                .setLongitude(position.getLongitude())
                .build();
    }

    private MobileDevice.TaskDTO buildTaskDTO(Task task) {
        return MobileDevice.TaskDTO.newBuilder().setId(task.getId())
                .setEndpointURI(task.getEndpoint().toString())
                .setParameters(task.getParameters())
                .setAssociatedWorkflowId(task.getAssociatedWorkflowId())
                .build();
    }
}
