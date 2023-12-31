package io.github.lorenzobucci.tesi.mobile_device.model;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.services_management.service.gRPC.OperationalGrpc;
import io.github.lorenzobucci.tesi.services_management.service.gRPC.ServicesManagementContract;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.*;

import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(name = "endpoint_URI", nullable = false, unique = true)
    private String endpointURI;
    private String parameters;

    @Embedded
    private DependabilityRequirements requirements;

    @Column(name = "associated_workflow_id")
    private long associatedWorkflowId;

    @Transient
    private final ServicesManagementClient servicesManagementClient = new ServicesManagementClient();

    Task(String endpointURI, String parameters, DependabilityRequirements requirements) {
        this.endpointURI = endpointURI;
        this.parameters = parameters;
        this.requirements = requirements;
        servicesManagementClient.requestService(requirements, endpointURI, parameters);
    }

    protected Task() {

    }

    void updateRequirements(DependabilityRequirements requirements) {
        this.requirements = requirements;
        servicesManagementClient.updateServiceRequirements(requirements);
    }

    void onCompleted() {
        servicesManagementClient.workflowCompleted();
    }

    public String getEndpointURI() {
        return endpointURI;
    }

    public String getParameters() {
        return parameters;
    }

    public DependabilityRequirements getRequirements() {
        return requirements;
    }

    public long getAssociatedWorkflowId() {
        return associatedWorkflowId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return endpointURI.equals(task.endpointURI);
    }

    @Override
    public int hashCode() {
        return endpointURI.hashCode();
    }


    private class ServicesManagementClient {

        private ManagedChannel channel;
        private OperationalGrpc.OperationalBlockingStub blockingStub;
        private OperationalGrpc.OperationalStub asyncStub;

        private void init() {
            String servicesManagementServiceHostname = System.getenv("SVC_MGMT_HOSTNAME") != null ?
                    System.getenv("SVC_MGMT_HOSTNAME") : "open-liberty-services-management";
            int servicesManagementServicePort = System.getenv("SVC_MGMT_PORT") != null ?
                    Integer.parseInt(System.getenv("SVC_MGMT_PORT")) : 9081;
            channel = ManagedChannelBuilder.forAddress(servicesManagementServiceHostname, servicesManagementServicePort).usePlaintext().build();
            blockingStub = OperationalGrpc.newBlockingStub(channel);
            asyncStub = OperationalGrpc.newStub(channel);
        }

        private void requestService(DependabilityRequirements requirements, String endpointURI, String parameters) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagementContract.WorkflowRequirementsDTO workflowRequirementsDTO =
                    ServicesManagementContract.WorkflowRequirementsDTO.newBuilder()
                            .setProximityComputation(requirements.isProximityComputation())
                            .setPreferredLatitude(requirements.getPreferredLatitude())
                            .setPreferredLongitude(requirements.getPreferredLongitude())
                            .build();

            ServicesManagementContract.WorkflowInstanceDTO workflowInstanceDTO = blockingStub.instantiateWorkflowInstance(ServicesManagementContract.InstantiateWorkflowParameters.newBuilder()
                    .setEndpointParameters(parameters)
                    .setEndpointURI(endpointURI)
                    .setWorkflowRequirements(workflowRequirementsDTO)
                    .build());

            channelShutdown();

            associatedWorkflowId = workflowInstanceDTO.getId();
        }

        private void updateServiceRequirements(DependabilityRequirements newRequirements) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagementContract.WorkflowRequirementsDTO workflowRequirementsDTO =
                    ServicesManagementContract.WorkflowRequirementsDTO.newBuilder()
                            .setProximityComputation(newRequirements.isProximityComputation())
                            .setPreferredLatitude(newRequirements.getPreferredLatitude())
                            .setPreferredLongitude(newRequirements.getPreferredLongitude())
                            .build();

            StreamObserver<Empty> streamObserver = new StreamObserver<>() {
                @Override
                public void onNext(Empty empty) {

                }

                @Override
                public void onError(Throwable throwable) {
                    // LOG FAILED UPDATE
                }

                @Override
                public void onCompleted() {

                }
            };

            asyncStub.updateWorkflowRequirements(ServicesManagementContract.UpdateRequirementsParameters.newBuilder()
                    .setWorkflowInstanceId(associatedWorkflowId)
                    .setNewWorkflowRequirements(workflowRequirementsDTO)
                    .build(), streamObserver);

            channelShutdown();
        }

        private void workflowCompleted() {
            init();

            StreamObserver<Empty> streamObserver = new StreamObserver<>() {
                @Override
                public void onNext(Empty empty) {

                }

                @Override
                public void onError(Throwable throwable) {
                    // LOG FAILED TERMINATION
                }

                @Override
                public void onCompleted() {

                }
            };

            asyncStub.terminateWorkflowInstance(Int64Value.of(associatedWorkflowId), streamObserver);

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
