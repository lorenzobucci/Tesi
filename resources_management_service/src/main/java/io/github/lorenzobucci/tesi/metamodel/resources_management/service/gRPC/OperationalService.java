package io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.AllocationController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.ContainerInstanceController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.NodeController;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import static io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.util.Builders.buildDependabilityRequirements;

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
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void destroyContainer(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            containerInstanceController.destroyContainerInstance(request.getValue());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void allocateContainer(ResourcesManagement.DependabilityRequirementsDTO request, StreamObserver<ResourcesManagement.ContainerInstanceDTO> responseObserver) {
        try {
            allocationController.allocateContainer(buildDependabilityRequirements(request));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void reviseContainerAllocation(ResourcesManagement.ReviseContainerAllocationParameters request, StreamObserver<ResourcesManagement.ContainerInstanceDTO> responseObserver) {
        try {
            allocationController.reviseContainerAllocation(request.getContainerId(), buildDependabilityRequirements(request.getNewRequirements()));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
