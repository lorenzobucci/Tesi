package io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.github.lorenzobucci.tesi.metamodel.services_management.controller.*;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.*;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;

import static io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.util.Builders.*;

public class CrudService extends CrudGrpc.CrudImplBase {

    @Inject
    private EndpointServiceTypeController endpointServiceTypeController;

    @Inject
    private EndpointServiceInstanceController endpointServiceInstanceController;

    @Inject
    private ServiceTypeController serviceTypeController;

    @Inject
    private ServiceInstanceController serviceInstanceController;

    @Inject
    private WorkflowTypeController workflowTypeController;

    @Inject
    private WorkflowInstanceController workflowInstanceController;

    @Override
    public void addEndpointServiceType(ServicesManagement.EndpointServiceTypeConstructorParameters request, StreamObserver<ServicesManagement.EndpointServiceTypeDTO> responseObserver) {
        try {
            EndpointServiceType endpointServiceType = endpointServiceTypeController.addEndpointServiceType(request.getServiceName(),
                    buildServiceRequirements(request.getServiceRequirements()),
                    URI.create(request.getPhysicalEndpointURI()));

            responseObserver.onNext(buildEndpointServiceTypeDTO(endpointServiceType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getEndpointServiceType(ServicesManagement.EndpointServiceTypeDTO request, StreamObserver<ServicesManagement.EndpointServiceTypeDTO> responseObserver) {
        try {
            EndpointServiceType endpointServiceType = endpointServiceTypeController.getEndpointServiceType(request.getId());
            responseObserver.onNext(buildEndpointServiceTypeDTO(endpointServiceType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getEndpointServiceTypeByURI(StringValue request, StreamObserver<ServicesManagement.EndpointServiceTypeDTO> responseObserver) {
        try {
            EndpointServiceType endpointServiceType = endpointServiceTypeController.getEndpointServiceType(URI.create(request.getValue()));
            responseObserver.onNext(buildEndpointServiceTypeDTO(endpointServiceType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveEndpointServiceTypes(Empty request, StreamObserver<ServicesManagement.EndpointServiceTypeList> responseObserver) {
        List<EndpointServiceType> endpointServiceTypes = endpointServiceTypeController.retrieveEndpointServiceTypes();
        ServicesManagement.EndpointServiceTypeList.Builder endpointServiceTypeList = ServicesManagement.EndpointServiceTypeList.newBuilder();

        for (EndpointServiceType endpointServiceType : endpointServiceTypes)
            endpointServiceTypeList.addEndpointServiceTypes(buildEndpointServiceTypeDTO(endpointServiceType));

        responseObserver.onNext(endpointServiceTypeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeEndpointServiceType(ServicesManagement.EndpointServiceTypeDTO request, StreamObserver<Empty> responseObserver) {
        try {
            endpointServiceTypeController.removeEndpointServiceType(request.getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getEndpointServiceInstance(ServicesManagement.EndpointServiceInstanceDTO request, StreamObserver<ServicesManagement.EndpointServiceInstanceDTO> responseObserver) {
        try {
            EndpointServiceInstance endpointServiceInstance = endpointServiceInstanceController.getEndpointServiceInstance(request.getId());
            responseObserver.onNext(buildEndpointServiceInstanceDTO(endpointServiceInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveEndpointServiceInstances(Empty request, StreamObserver<ServicesManagement.EndpointServiceInstanceList> responseObserver) {
        List<EndpointServiceInstance> endpointServiceInstances = endpointServiceInstanceController.retrieveEndpointServiceInstances();
        ServicesManagement.EndpointServiceInstanceList.Builder endpointServiceInstanceList = ServicesManagement.EndpointServiceInstanceList.newBuilder();

        for (EndpointServiceInstance endpointServiceInstance : endpointServiceInstances)
            endpointServiceInstanceList.addEndpointServiceInstances(buildEndpointServiceInstanceDTO(endpointServiceInstance));

        responseObserver.onNext(endpointServiceInstanceList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getServiceInstance(ServicesManagement.ServiceInstanceDTO request, StreamObserver<ServicesManagement.ServiceInstanceDTO> responseObserver) {
        try {
            ServiceInstance serviceInstance = serviceInstanceController.getServiceInstance(request.getId());
            responseObserver.onNext(buildServiceInstanceDTO(serviceInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveServiceInstances(Empty request, StreamObserver<ServicesManagement.ServiceInstanceList> responseObserver) {
        List<ServiceInstance> serviceInstances = serviceInstanceController.retrieveServiceInstances();
        ServicesManagement.ServiceInstanceList.Builder serviceInstanceList = ServicesManagement.ServiceInstanceList.newBuilder();

        for (ServiceInstance serviceInstance : serviceInstances)
            serviceInstanceList.addServiceInstances(buildServiceInstanceDTO(serviceInstance));

        responseObserver.onNext(serviceInstanceList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addServiceType(ServicesManagement.ServiceTypeConstructorParameters request, StreamObserver<ServicesManagement.ServiceTypeDTO> responseObserver) {
        try {
            ServiceType serviceType = serviceTypeController.addServiceType(request.getServiceName(), buildServiceRequirements(request.getServiceRequirements()));

            responseObserver.onNext(buildServiceTypeDTO(serviceType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getServiceType(ServicesManagement.ServiceTypeDTO request, StreamObserver<ServicesManagement.ServiceTypeDTO> responseObserver) {
        try {
            ServiceType serviceType = serviceTypeController.getServiceType(request.getId());
            responseObserver.onNext(buildServiceTypeDTO(serviceType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveServiceTypes(Empty request, StreamObserver<ServicesManagement.ServiceTypeList> responseObserver) {
        List<ServiceType> serviceTypes = serviceTypeController.retrieveServiceTypes();
        ServicesManagement.ServiceTypeList.Builder serviceTypeList = ServicesManagement.ServiceTypeList.newBuilder();

        for (ServiceType serviceType : serviceTypes)
            serviceTypeList.addServiceTypes(buildServiceTypeDTO(serviceType));

        responseObserver.onNext(serviceTypeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeServiceType(ServicesManagement.ServiceTypeDTO request, StreamObserver<Empty> responseObserver) {
        try {
            serviceTypeController.removeServiceType(request.getId());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getWorkflowInstance(ServicesManagement.WorkflowInstanceDTO request, StreamObserver<ServicesManagement.WorkflowInstanceDTO> responseObserver) {
        try {
            WorkflowInstance workflowInstance = workflowInstanceController.getWorkflowInstance(request.getId());
            responseObserver.onNext(buildWorkflowInstanceDTO(workflowInstance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveWorkflowInstances(Empty request, StreamObserver<ServicesManagement.WorkflowInstanceList> responseObserver) {
        List<WorkflowInstance> workflowInstances = workflowInstanceController.retrieveWorkflowInstances();
        ServicesManagement.WorkflowInstanceList.Builder workflowInstanceList = ServicesManagement.WorkflowInstanceList.newBuilder();

        for (WorkflowInstance workflowInstance : workflowInstances)
            workflowInstanceList.addWorkflowInstances(buildWorkflowInstanceDTO(workflowInstance));

        responseObserver.onNext(workflowInstanceList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addWorkflowType(ServicesManagement.WorkflowTypeConstructorParameters request, StreamObserver<ServicesManagement.WorkflowTypeDTO> responseObserver) {
        try {
            WorkflowType workflowType = workflowTypeController.addWorkflowType(request.getEndpointServiceType().getId());

            responseObserver.onNext(buildWorkflowTypeDTO(workflowType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getWorkflowType(ServicesManagement.WorkflowTypeDTO request, StreamObserver<ServicesManagement.WorkflowTypeDTO> responseObserver) {
        try {
            WorkflowType workflowType = workflowTypeController.getWorkflowType(request.getId());
            responseObserver.onNext(buildWorkflowTypeDTO(workflowType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getWorkflowTypeByURI(StringValue request, StreamObserver<ServicesManagement.WorkflowTypeDTO> responseObserver) {
        try {
            WorkflowType workflowType = workflowTypeController.getWorkflowType(URI.create(request.getValue()));
            responseObserver.onNext(buildWorkflowTypeDTO(workflowType));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void retrieveWorkflowTypes(Empty request, StreamObserver<ServicesManagement.WorkflowTypeList> responseObserver) {
        List<WorkflowType> workflowTypes = workflowTypeController.retrieveWorkflowTypes();
        ServicesManagement.WorkflowTypeList.Builder workflowTypeList = ServicesManagement.WorkflowTypeList.newBuilder();

        for (WorkflowType workflowType : workflowTypes)
            workflowTypeList.addWorkflowTypes(buildWorkflowTypeDTO(workflowType));

        responseObserver.onNext(workflowTypeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeWorkflowType(ServicesManagement.RemoveWorkflowTypeParameters request, StreamObserver<Empty> responseObserver) {
        try {
            workflowTypeController.removeWorkflowType(request.getWorkflowType().getId(), request.getRemoveEndpointServiceType());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

}
