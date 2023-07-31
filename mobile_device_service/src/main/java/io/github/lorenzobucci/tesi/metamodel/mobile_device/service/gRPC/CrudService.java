package io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.controller.MobileDeviceDTController;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.controller.TaskController;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Task;
import io.grpc.stub.StreamObserver;
import io.openliberty.grpc.annotation.GrpcService;
import jakarta.inject.Inject;

import java.util.List;

import static io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.util.Builders.buildMobileDeviceDTDTO;
import static io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.util.Builders.buildTaskDTO;

@GrpcService
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
    public void getMobileDeviceDT(Int64Value request, StreamObserver<MobileDevice.MobileDeviceDTDTO> responseObserver) {
        try {
            MobileDeviceDT mobileDeviceDT = mobileDeviceDTController.getMobileDeviceDT(request.getValue());
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
    public void removeMobileDeviceDT(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.removeMobileDeviceDT(request.getValue());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getTask(Int64Value request, StreamObserver<MobileDevice.TaskDTO> responseObserver) {
        try {
            Task task = taskController.getTask(request.getValue());
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

}
