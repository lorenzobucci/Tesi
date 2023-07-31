package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.OperationalGrpc;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.ServicesManagementContract;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.*;

import java.net.URI;

@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(nullable = false, unique = true)
    private URI endpoint;
    private String parameters;

    @Embedded
    private DependabilityRequirements requirements;

    @Column(name = "associated_workflow_id")
    private long associatedWorkflowId;

    @Transient
    private final ServicesManagementsClient servicesManagementsClient = new ServicesManagementsClient();

    Task(URI endpoint, String parameters, DependabilityRequirements requirements) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.requirements = requirements;
        servicesManagementsClient.requestService(requirements, endpoint, parameters);
    }

    protected Task() {

    }

    void updateRequirements(DependabilityRequirements requirements) {
        this.requirements = requirements;
        servicesManagementsClient.updateServiceRequirements(requirements);
    }

    void onCompleted() {
        servicesManagementsClient.workflowCompleted();
    }

    public URI getEndpoint() {
        return endpoint;
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

        return endpoint.equals(task.endpoint);
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }


    private class ServicesManagementsClient {

        private ManagedChannel channel;
        private OperationalGrpc.OperationalBlockingStub blockingStub;
        private OperationalGrpc.OperationalStub asyncStub;

        private void init() {
            channel = ManagedChannelBuilder.forAddress("localhost", 9081).usePlaintext().build();
            blockingStub = OperationalGrpc.newBlockingStub(channel);
            asyncStub = OperationalGrpc.newStub(channel);
        }

        private void requestService(DependabilityRequirements requirements, URI endpoint, String parameters) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagementContract.WorkflowRequirementsDTO workflowRequirementsDTO = ServicesManagementContract.WorkflowRequirementsDTO.newBuilder().build();

            ServicesManagementContract.WorkflowInstanceDTO workflowInstanceDTO = blockingStub.instantiateWorkflowInstance(ServicesManagementContract.InstantiateWorkflowParameters.newBuilder()
                    .setEndpointParameters(parameters)
                    .setEndpointURI(endpoint.toString())
                    .setWorkflowRequirements(workflowRequirementsDTO)
                    .build());

            channel.shutdown();

            associatedWorkflowId = workflowInstanceDTO.getId();
        }

        private void updateServiceRequirements(DependabilityRequirements newRequirements) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagementContract.WorkflowRequirementsDTO workflowRequirementsDTO = ServicesManagementContract.WorkflowRequirementsDTO.newBuilder().build();

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

            channel.shutdown();
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

            channel.shutdown();
        }
    }
}
