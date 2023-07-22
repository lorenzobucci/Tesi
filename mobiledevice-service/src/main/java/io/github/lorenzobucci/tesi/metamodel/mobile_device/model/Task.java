package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

import com.google.protobuf.Empty;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.OperationalGrpc;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.ServicesManagement;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.net.URI;

@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(nullable = false)
    private URI endpoint;
    private String parameters;

    @Embedded
    private DependabilityRequirements requirements;

    @Column(name = "associated_workflow_id")
    private long associatedWorkflowId;

    Task(URI endpoint, String parameters, DependabilityRequirements requirements) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.requirements = requirements;
        associatedWorkflowId = ServicesManagementsClient.requestService(requirements, endpoint, parameters);
    }

    protected Task() {

    }

    void updateRequirements(DependabilityRequirements requirements) {
        this.requirements = requirements;
        ServicesManagementsClient.updateServiceRequirements(associatedWorkflowId, requirements);
    }

    void onCompleted() {
        ServicesManagementsClient.workflowCompleted(associatedWorkflowId);
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

    static class ServicesManagementsClient {

        static ManagedChannel channel;
        static OperationalGrpc.OperationalBlockingStub blockingStub;
        static OperationalGrpc.OperationalStub asyncStub;

        private static void init() {
            channel = ManagedChannelBuilder.forAddress("localhost", 9001).usePlaintext().build();
            blockingStub = OperationalGrpc.newBlockingStub(channel);
            asyncStub = OperationalGrpc.newStub(channel);
        }

        private static long requestService(DependabilityRequirements requirements, URI endpoint, String parameters) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagement.WorkflowRequirementsDTO workflowRequirementsDTO = ServicesManagement.WorkflowRequirementsDTO.newBuilder().build();

            ServicesManagement.WorkflowInstanceDTO workflowInstanceDTO = blockingStub.instantiateWorkflowInstance(ServicesManagement.InstantiateWorkflowParameters.newBuilder()
                    .setEndpointParameters(parameters)
                    .setEndpointURI(endpoint.toString())
                    .setWorkflowRequirements(workflowRequirementsDTO)
                    .build());

            channel.shutdown();

            return workflowInstanceDTO.getId();
        }

        private static void updateServiceRequirements(long associatedWorkflowId, DependabilityRequirements newRequirements) {
            init();

            // DO CONVERSION FROM DependabilityRequirements TO WorkflowRequirementsDTO
            ServicesManagement.WorkflowRequirementsDTO workflowRequirementsDTO = ServicesManagement.WorkflowRequirementsDTO.newBuilder().build();

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

            asyncStub.updateWorkflowRequirements(ServicesManagement.UpdateRequirementsParameters.newBuilder()
                    .setWorkflowInstance(ServicesManagement.WorkflowInstanceDTO.newBuilder().setId(associatedWorkflowId).build())
                    .setNewWorkflowRequirements(workflowRequirementsDTO)
                    .build(), streamObserver);

            channel.shutdown();
        }

        private static void workflowCompleted(long associatedWorkflowId) {
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

            asyncStub.terminateWorkflowInstance(ServicesManagement.WorkflowInstanceDTO.newBuilder().setId(associatedWorkflowId).build(),
                    streamObserver);

            channel.shutdown();
        }
    }
}
