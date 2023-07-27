package io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.controller.MobileDeviceDTController;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Position;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.net.URI;
import java.sql.Timestamp;

public class OperationalService extends OperationalGrpc.OperationalImplBase {

    @Inject
    private MobileDeviceDTController mobileDeviceDTController;

    @Override
    public void signalMobileDeviceEndpointInvocation(MobileDevice.EndpointInvocationParameters request, StreamObserver<Empty> responseObserver) {
        try {
            mobileDeviceDTController.signalMobileDeviceEndpointInvocation(request.getMobileDeviceDTId(),
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
            mobileDeviceDTController.signalMobileDeviceTaskCompletion(request.getMobileDeviceDTId(), request.getCompletedTaskId());
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

            mobileDeviceDTController.syncMobileDeviceDTProperties(request.getMobileDeviceDTId(), position);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
