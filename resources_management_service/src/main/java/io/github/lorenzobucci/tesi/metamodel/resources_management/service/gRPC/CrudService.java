package io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.AllocatorAlgorithm;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.AllocationController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.ContainerInstanceController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.ContainerTypeController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.controller.NodeController;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerType;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.Node;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.NodeTechnicalProperties;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.net.InetAddress;
import java.util.List;

import static io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.util.Builders.*;

public class CrudService extends CrudGrpc.CrudImplBase {

    @Inject
    private AllocationController allocationController;

    @Inject
    private ContainerInstanceController containerInstanceController;

    @Inject
    private ContainerTypeController containerTypeController;

    @Inject
    private NodeController nodeController;

    @Override
    public void addNode(ResourcesManagement.NodeConstructorParameters request, StreamObserver<ResourcesManagement.NodeDTO> responseObserver) {
        try {
            NodeTechnicalProperties properties = new NodeTechnicalProperties(request.getProperties().getMemoryMB(),
                    request.getProperties().getCpuCoresNumber(),
                    request.getProperties().getCpuFrequencyGHz(),
                    request.getProperties().getInstalledOS());
            Node node = nodeController.addNode(Node.NodeType.valueOf(request.getNodeType().toString()),
                    InetAddress.getByName(request.getIpAddress()),
                    properties,
                    request.getLatitude(),
                    request.getLongitude());

            responseObserver.onNext(buildNodeDTO(node));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getNode(Int64Value request, StreamObserver<ResourcesManagement.NodeDTO> responseObserver) {
        try {
            Node node = nodeController.getNode(request.getValue());
            responseObserver.onNext(buildNodeDTO(node));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveNodes(Empty request, StreamObserver<ResourcesManagement.NodeList> responseObserver) {
        List<Node> nodes = nodeController.retrieveNodes();
        ResourcesManagement.NodeList.Builder nodeList = ResourcesManagement.NodeList.newBuilder();

        for (Node node : nodes)
            nodeList.addNodes(buildNodeDTO(node));

        responseObserver.onNext(nodeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeNode(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            nodeController.removeNode(request.getValue());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addContainerType(ResourcesManagement.ContainerTypeConstructorParameters request, StreamObserver<ResourcesManagement.ContainerTypeDTO> responseObserver) {
        ContainerType containerType = containerTypeController.addContainerType(request.getImageName(), request.getImageVersion());

        responseObserver.onNext(buildContainerTypeDTO(containerType));
        responseObserver.onCompleted();
    }

    @Override
    public void getContainerType(Int64Value request, StreamObserver<ResourcesManagement.ContainerTypeDTO> responseObserver) {
        try {
            ContainerType containerType = containerTypeController.getContainerType(request.getValue());
            responseObserver.onNext(buildContainerTypeDTO(containerType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveContainerTypes(Empty request, StreamObserver<ResourcesManagement.ContainerTypeList> responseObserver) {
        List<ContainerType> containerTypes = containerTypeController.retrieveContainerTypes();
        ResourcesManagement.ContainerTypeList.Builder containerTypeList = ResourcesManagement.ContainerTypeList.newBuilder();

        for (ContainerType containerType : containerTypes)
            containerTypeList.addContainerTypes(buildContainerTypeDTO(containerType));

        responseObserver.onNext(containerTypeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeContainerType(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            containerTypeController.removeContainerType(request.getValue());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getContainerInstance(Int64Value request, StreamObserver<ResourcesManagement.ContainerInstanceDTO> responseObserver) {
        try {
            ContainerInstance containerInstance = containerInstanceController.getContainerInstance(request.getValue());
            responseObserver.onNext(buildContainerInstanceDTO(containerInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveContainerInstances(Empty request, StreamObserver<ResourcesManagement.ContainerInstanceList> responseObserver) {
        List<ContainerInstance> containerInstances = containerInstanceController.retrieveContainerInstances();
        ResourcesManagement.ContainerInstanceList.Builder containerInstanceList = ResourcesManagement.ContainerInstanceList.newBuilder();

        for (ContainerInstance containerInstance : containerInstances)
            containerInstanceList.addContainerInstances(buildContainerInstanceDTO(containerInstance));

        responseObserver.onNext(containerInstanceList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrentAllocator(Empty request, StreamObserver<StringValue> responseObserver) {
        try {
            AllocatorAlgorithm allocatorAlgorithm = allocationController.getCurrentAllocator();
            responseObserver.onNext(StringValue.of(allocatorAlgorithm.getClass().getName()));
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void setAllocatorAlgorithm(StringValue request, StreamObserver<Empty> responseObserver) {
        try {
            allocationController.setAllocatorAlgorithm((AllocatorAlgorithm) Class.forName(request.getValue()).getConstructor().newInstance());
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
