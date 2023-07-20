package io.github.lorenzobucci.tesi.metamodel.mobiledevice.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.controller.MobileDeviceDTController;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Position;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Task;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

public class MobileDeviceDTService extends MobileDeviceDTServiceGrpc.MobileDeviceDTServiceImplBase {

    @Inject
    private MobileDeviceDTController mobileDeviceDTController;

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
    public void signalMobileDeviceEndpointInvocation(MobileDevice.EndpointInvocationParameters request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.signalMobileDeviceEndpointInvocation(request.getMobileDeviceDT().getId(),
                    URI.create(request.getInvokedEndpoint()),
                    request.getParameters());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void signalMobileDeviceTaskCompletion(MobileDevice.TaskCompletionParameters request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.signalMobileDeviceTaskCompletion(request.getMobileDeviceDT().getId(), request.getCompletedTask().getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void syncMobileDeviceDTProperties(MobileDevice.MobileDeviceDTSyncParameters request, StreamObserver<Empty> responseObserver) {
        try {
            MobileDevice.PositionDTO positionDTO = request.getCurrentPosition();
            Position position = new Position(positionDTO.getLatitude(), positionDTO.getLongitude(), new Timestamp(Timestamps.toMillis(positionDTO.getTimestamp())));

            mobileDeviceDTController.syncMobileDeviceDTProperties(request.getMobileDeviceDT().getId(), position);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private MobileDevice.MobileDeviceDTDTO buildMobileDeviceDTDTO(MobileDeviceDT mobileDeviceDT) {
        MobileDevice.MobileDeviceDTDTO.Builder mobileDeviceDTDTO = MobileDevice.MobileDeviceDTDTO.newBuilder().setId(mobileDeviceDT.getId());

        for (Task task : mobileDeviceDT.getRunningTasks()) {
            MobileDevice.TaskDTO taskDTO = MobileDevice.TaskDTO.newBuilder().setId(task.getId())
                    .setEndpointURI(task.getEndpoint().toString())
                    .setParameters(task.getParameters())
                    .build();
            mobileDeviceDTDTO.addRunningTasks(taskDTO);
        }

        for (Position position : mobileDeviceDT.getPastTrajectory().getPositionsSet()) {
            MobileDevice.PositionDTO positionDTO = MobileDevice.PositionDTO.newBuilder()
                    .setTimestamp(Timestamps.fromMillis(position.getTimestamp().getTime()))
                    .setLatitude(position.getLatitude())
                    .setLongitude(position.getLongitude())
                    .build();
            mobileDeviceDTDTO.addTrajectory(positionDTO);
        }

        return mobileDeviceDTDTO.build();
    }
}
