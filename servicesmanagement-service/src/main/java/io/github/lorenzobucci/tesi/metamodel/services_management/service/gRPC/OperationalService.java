package io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import io.github.lorenzobucci.tesi.metamodel.services_management.controller.WorkflowInstanceController;
import io.github.lorenzobucci.tesi.metamodel.services_management.controller.WorkflowTypeController;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.ServiceType;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.WorkflowInstance;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.Set;

import static io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.util.Builders.*;

public class OperationalService extends OperationalGrpc.OperationalImplBase {

    @Inject
    private WorkflowInstanceController workflowInstanceController;

    @Inject
    private WorkflowTypeController workflowTypeController;

    @Override
    public void instantiateWorkflowInstance(ServicesManagement.InstantiateWorkflowParameters request, StreamObserver<ServicesManagement.WorkflowInstanceDTO> responseObserver) {
        try {
            WorkflowInstance workflowInstance = workflowInstanceController.instantiateWorkflowInstance(URI.create(request.getEndpointURI()),
                    request.getEndpointParameters(),
                    buildWorkflowRequirements(request.getWorkflowRequirements()));
            responseObserver.onNext(buildWorkflowInstanceDTO(workflowInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void terminateWorkflowInstance(ServicesManagement.WorkflowInstanceDTO request, StreamObserver<Empty> responseObserver) {
        try {
            workflowInstanceController.terminateWorkflowInstance(request.getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateWorkflowRequirements(ServicesManagement.UpdateRequirementsParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowInstanceController.updateWorkflowRequirements(request.getWorkflowInstance().getId(),
                    buildWorkflowRequirements(request.getNewWorkflowRequirements()));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addServiceTypeToWorkflowType(ServicesManagement.AddServiceTypeToWorkflowTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.addServiceTypeToWorkflow(request.getWorkflowType().getId(),
                    request.getServiceTypeToAdd().getId(),
                    request.getCallerServiceType().getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addRPCToWorkflowType(ServicesManagement.AddRPCToWorkflowTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.addRPCToWorkflow(request.getWorkflowType().getId(),
                    request.getCallerServiceType().getId(),
                    request.getCalleeServiceType().getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void workflowTypeContainsServiceType(ServicesManagement.WorkflowTypeContainsServiceTypeParameters request, StreamObserver<BoolValue> responseObserver) {
        try {
            boolean response = workflowTypeController.workflowTypeContainsServiceType(request.getWorkflowType().getId(),
                    request.getServiceTypeToVerify().getId());
            responseObserver.onNext(BoolValue.of(response));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void removeServiceTypeFromWorkflow(ServicesManagement.RemoveServiceTypeFromWorkflowParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.removeServiceTypeFromWorkflow(request.getWorkflowType().getId(),
                    request.getServiceTypeToRemove().getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateWorkflowEndpointServiceType(ServicesManagement.UpdateWorkflowEndpointServiceTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.updateWorkflowEndpointServiceType(request.getWorkflowType().getId(),
                    request.getNewEndpointServiceType().getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveServiceTypesFromWorkflow(ServicesManagement.WorkflowTypeDTO request, StreamObserver<ServicesManagement.ServiceTypeList> responseObserver) {
        try {
            Set<ServiceType> serviceTypes = workflowTypeController.retrieveServiceTypesFromWorkflow(request.getId());
            ServicesManagement.ServiceTypeList.Builder serviceTypeList = ServicesManagement.ServiceTypeList.newBuilder();

            for (ServiceType serviceType : serviceTypes)
                serviceTypeList.addServiceTypes(buildServiceTypeDTO(serviceType));

            responseObserver.onNext(serviceTypeList.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

}
