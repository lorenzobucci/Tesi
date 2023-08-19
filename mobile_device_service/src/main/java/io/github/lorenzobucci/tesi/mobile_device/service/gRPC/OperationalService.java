package io.github.lorenzobucci.tesi.mobile_device.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.mobile_device.controller.MobileDeviceDTController;
import io.github.lorenzobucci.tesi.mobile_device.model.Position;
import io.github.lorenzobucci.tesi.mobile_device.model.TrajectoryForecaster;
import io.grpc.stub.StreamObserver;
import io.openliberty.grpc.annotation.GrpcService;
import jakarta.inject.Inject;

import java.sql.Timestamp;

@GrpcService
public class OperationalService extends OperationalGrpc.OperationalImplBase {

    @Inject
    private MobileDeviceDTController mobileDeviceDTController;

    @Override
    public void signalMobileDeviceEndpointInvocation(MobileDevice.EndpointInvocationParameters request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.signalMobileDeviceEndpointInvocation(request.getMobileDeviceDTId(),
                    request.getInvokedEndpointURI(),
                    request.getParameters());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void signalMobileDeviceTaskCompletion(MobileDevice.MobileDeviceDTTaskEndpoint request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.signalMobileDeviceTaskCompletion(request.getMobileDeviceDTId(), request.getTaskEndpointURI());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void syncMobileDeviceDTProperties(MobileDevice.SyncMobileDeviceDTParameters request, StreamObserver<Empty> responseObserver) {
        try {
            MobileDevice.PositionDTO positionDTO = request.getCurrentPosition();
            Position position = new Position(positionDTO.getLatitude(), positionDTO.getLongitude(), new Timestamp(Timestamps.toMillis(positionDTO.getTimestamp())));

            mobileDeviceDTController.syncMobileDeviceDTProperties(request.getMobileDeviceDTId(), position);
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void setMobileDeviceDTTrajectoryForecaster(MobileDevice.SetTrajectoryForecasterParameters request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.setMobileDeviceDTTrajectoryForecaster(request.getMobileDeviceDTId(),
                    (TrajectoryForecaster) Class.forName(request.getTrajectoryForecasterClassName()).getConstructor().newInstance());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void requestMobileDeviceDTTaskOptimization(MobileDevice.MobileDeviceDTTaskEndpoint request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.requestMobileDeviceDTTaskOptimization(request.getMobileDeviceDTId(), request.getTaskEndpointURI());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
