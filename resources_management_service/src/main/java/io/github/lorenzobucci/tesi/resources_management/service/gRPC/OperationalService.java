package io.github.lorenzobucci.tesi.resources_management.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.resources_management.controller.AllocationController;
import io.github.lorenzobucci.tesi.resources_management.controller.ContainerInstanceController;
import io.github.lorenzobucci.tesi.resources_management.controller.NodeController;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import io.grpc.stub.StreamObserver;
import io.openliberty.grpc.annotation.GrpcService;
import jakarta.inject.Inject;

import static io.github.lorenzobucci.tesi.resources_management.service.gRPC.util.Builders.buildContainerInstanceDTO;
import static io.github.lorenzobucci.tesi.resources_management.service.gRPC.util.Builders.buildDependabilityRequirements;

@GrpcService
public class OperationalService extends OperationalGrpc.OperationalImplBase {

    @Inject
    private AllocationController allocationController;

    @Inject
    private ContainerInstanceController containerInstanceController;

    @Inject
    private NodeController nodeController;

    @Override
    public void syncNodeDTProperties(ResourcesManagement.NodeDTSyncParameters request, StreamObserver<Empty> responseObserver) {
        try {
            nodeController.syncNodeDTProperties(request.getNodeId(), request.getMemoryUsagePercentage(), request.getCpuUsagePercentage());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void destroyContainer(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            containerInstanceController.destroyContainerInstance(request.getValue());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void allocateContainer(ResourcesManagement.DependabilityRequirementsDTO request, StreamObserver<ResourcesManagement.ContainerInstanceDTO> responseObserver) {
        try {
            ContainerInstance containerInstance = allocationController.allocateContainer(buildDependabilityRequirements(request));
            responseObserver.onNext(buildContainerInstanceDTO(containerInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void reviseContainerAllocation(ResourcesManagement.ReviseContainerAllocationParameters request, StreamObserver<ResourcesManagement.ContainerInstanceDTO> responseObserver) {
        try {
            ContainerInstance containerInstance = allocationController.reviseContainerAllocation(request.getContainerInstanceId(), buildDependabilityRequirements(request.getNewRequirements()));
            responseObserver.onNext(buildContainerInstanceDTO(containerInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void syncContainerDTProperties(ResourcesManagement.ContainerDTSyncProperties request, StreamObserver<Empty> responseObserver) {
        try {
            containerInstanceController.syncContainerDTProperties(request.getContainerId(), request.getContainerState());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
