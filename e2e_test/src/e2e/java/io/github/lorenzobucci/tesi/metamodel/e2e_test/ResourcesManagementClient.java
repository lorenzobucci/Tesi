package io.github.lorenzobucci.tesi.metamodel.e2e_test;

import io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.CrudGrpc;
import io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.OperationalGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ResourcesManagementClient {

    private final CrudGrpc.CrudBlockingStub crudBlockingStub;
    private final OperationalGrpc.OperationalBlockingStub operationalBlockingStub;

    public ResourcesManagementClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9082).usePlaintext().build();
        crudBlockingStub = CrudGrpc.newBlockingStub(channel);
        operationalBlockingStub = OperationalGrpc.newBlockingStub(channel);
    }

    public CrudGrpc.CrudBlockingStub getCrudBlockingStub() {
        return crudBlockingStub;
    }

    public OperationalGrpc.OperationalBlockingStub getOperationalBlockingStub() {
        return operationalBlockingStub;
    }
}

