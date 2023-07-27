package io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
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
    public void terminateWorkflowInstance(Int64Value request, StreamObserver<Empty> responseObserver) {
        try {
            workflowInstanceController.terminateWorkflowInstance(request.getValue());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateWorkflowRequirements(ServicesManagement.UpdateRequirementsParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowInstanceController.updateWorkflowRequirements(request.getWorkflowInstanceId(),
                    buildWorkflowRequirements(request.getNewWorkflowRequirements()));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addServiceTypeToWorkflowType(ServicesManagement.AddServiceTypeToWorkflowTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.addServiceTypeToWorkflow(request.getWorkflowTypeId(),
                    request.getServiceTypeToAddId(),
                    request.getCallerServiceTypeId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addRPCToWorkflowType(ServicesManagement.AddRPCToWorkflowTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.addRPCToWorkflow(request.getWorkflowTypeId(),
                    request.getCallerServiceTypeId(),
                    request.getCalleeServiceTypeId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void workflowTypeContainsServiceType(ServicesManagement.WorkflowTypeContainsServiceTypeParameters request, StreamObserver<BoolValue> responseObserver) {
        try {
            boolean response = workflowTypeController.workflowTypeContainsServiceType(request.getWorkflowTypeId(),
                    request.getServiceTypeToVerifyId());
            responseObserver.onNext(BoolValue.of(response));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void removeServiceTypeFromWorkflow(ServicesManagement.RemoveServiceTypeFromWorkflowParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.removeServiceTypeFromWorkflow(request.getWorkflowTypeId(),
                    request.getServiceTypeToRemoveId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateWorkflowEndpointServiceType(ServicesManagement.UpdateWorkflowEndpointServiceTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.updateWorkflowEndpointServiceType(request.getWorkflowTypeId(),
                    request.getNewEndpointServiceTypeId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveServiceTypesFromWorkflow(Int64Value request, StreamObserver<ServicesManagement.ServiceTypeList> responseObserver) {
        try {
            Set<ServiceType> serviceTypes = workflowTypeController.retrieveServiceTypesFromWorkflow(request.getValue());
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