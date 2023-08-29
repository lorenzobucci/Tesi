package io.github.lorenzobucci.tesi.services_management.model;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.resources_management.service.gRPC.OperationalGrpc;
import io.github.lorenzobucci.tesi.resources_management.service.gRPC.ResourcesManagementContract;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Embeddable
public class Container {

    @Column(name = "associated_container_id", unique = true)
    private long associatedContainerId;

    @Column(name = "container_ip_address")
    private InetAddress ipAddress;

    @Transient
    private final ResourcesManagementClient resourcesManagementClient = new ResourcesManagementClient();

    public Container(ServiceRequirements serviceRequirements, WorkflowRequirements workflowRequirements) {
        resourcesManagementClient.allocateContainer(serviceRequirements, workflowRequirements);
    }

    protected Container() {

    }

    void optimize(ServiceRequirements serviceRequirements, WorkflowRequirements newWorkflowRequirements) {
        resourcesManagementClient.reviseContainerAllocation(serviceRequirements, newWorkflowRequirements);
    }

    void destroy() {
        resourcesManagementClient.destroyContainer();
    }

    public long getAssociatedContainerId() {
        return associatedContainerId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    // THIS CLASS HAS THE RESPONSIBILITY OF MERGING SERVICE AND WORKFLOW REQUIREMENTS
    private record DependabilityRequirements(boolean proximityComputation, float preferredLatitude,
                                             float preferredLongitude) { // SAMPLE REQUIREMENTS

        private static DependabilityRequirements mergeAndBuild(ServiceRequirements serviceRequirements, WorkflowRequirements workflowRequirements) {
            // SIMULATION MERGE ServiceRequirements AND WorkflowRequirements
            return new DependabilityRequirements(workflowRequirements.isProximityComputation(), workflowRequirements.getPreferredLatitude(), workflowRequirements.getPreferredLongitude());
        }

    }

    private class ResourcesManagementClient {
        private ManagedChannel channel;
        private OperationalGrpc.OperationalBlockingStub blockingStub;
        private OperationalGrpc.OperationalStub asyncStub;

        private void init() {
            String resourcesManagementServiceHostname = System.getenv("RES_MGMT_HOSTNAME") != null ?
                    System.getenv("RES_MGMT_HOSTNAME") : "open-liberty-resources-management";
            int resourcesManagementServicePort = System.getenv("RES_MGMT_PORT") != null ?
                    Integer.parseInt(System.getenv("RES_MGMT_PORT")) : 9082;
            channel = ManagedChannelBuilder.forAddress(resourcesManagementServiceHostname, resourcesManagementServicePort).usePlaintext().build();
            blockingStub = OperationalGrpc.newBlockingStub(channel);
            asyncStub = OperationalGrpc.newStub(channel);
        }

        private void allocateContainer(ServiceRequirements serviceRequirements, WorkflowRequirements workflowRequirements) {
            init();

            DependabilityRequirements dependabilityRequirements = DependabilityRequirements.mergeAndBuild(serviceRequirements, workflowRequirements);

            // DO CONVERSION FROM DependabilityRequirements TO DependabilityRequirementsDTO
            ResourcesManagementContract.DependabilityRequirementsDTO dependabilityRequirementsDTO =
                    ResourcesManagementContract.DependabilityRequirementsDTO.newBuilder()
                            .setProximityComputation(dependabilityRequirements.proximityComputation)
                            .setPreferredLatitude(dependabilityRequirements.preferredLatitude)
                            .setPreferredLongitude(dependabilityRequirements.preferredLongitude)
                            .build();

            ResourcesManagementContract.ContainerInstanceDTO containerInstanceDTO = blockingStub.allocateContainer(dependabilityRequirementsDTO);
            channelShutdown();

            try {
                ipAddress = InetAddress.getByName(containerInstanceDTO.getNodeIpAddress());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            associatedContainerId = containerInstanceDTO.getId();
        }

        private void reviseContainerAllocation(ServiceRequirements serviceRequirements, WorkflowRequirements newWorkflowRequirements) {
            init();

            DependabilityRequirements dependabilityRequirements = DependabilityRequirements.mergeAndBuild(serviceRequirements, newWorkflowRequirements);

            // DO CONVERSION FROM DependabilityRequirements TO DependabilityRequirementsDTO
            ResourcesManagementContract.DependabilityRequirementsDTO dependabilityRequirementsDTO =
                    ResourcesManagementContract.DependabilityRequirementsDTO.newBuilder()
                            .setProximityComputation(dependabilityRequirements.proximityComputation)
                            .setPreferredLatitude(dependabilityRequirements.preferredLatitude)
                            .setPreferredLongitude(dependabilityRequirements.preferredLongitude)
                            .build();

            StreamObserver<ResourcesManagementContract.ContainerInstanceDTO> streamObserver = new StreamObserver<>() {
                @Override
                public void onNext(ResourcesManagementContract.ContainerInstanceDTO containerInstanceDTO) {
                    try {
                        ipAddress = InetAddress.getByName(containerInstanceDTO.getNodeIpAddress());
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    // LOG FAILED REVISE
                }

                @Override
                public void onCompleted() {

                }
            };

            asyncStub.reviseContainerAllocation(ResourcesManagementContract.ReviseContainerAllocationParameters.newBuilder()
                    .setContainerInstanceId(associatedContainerId)
                    .setNewRequirements(dependabilityRequirementsDTO)
                    .build(), streamObserver);

            channelShutdown();
        }

        public void destroyContainer() {
            init();

            StreamObserver<Empty> streamObserver = new StreamObserver<>() {
                @Override
                public void onNext(Empty empty) {

                }

                @Override
                public void onError(Throwable throwable) {
                    // LOG FAILED DESTROY
                }

                @Override
                public void onCompleted() {

                }
            };

            asyncStub.destroyContainer(Int64Value.of(associatedContainerId), streamObserver);

            channelShutdown();
        }

        private void channelShutdown() {
            channel.shutdown();
            try {
                channel.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
