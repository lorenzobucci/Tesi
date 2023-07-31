package io.github.lorenzobucci.tesi.metamodel.e2e_test;

import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.CrudGrpc;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.OperationalGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ServicesManagementClient {
    private final CrudGrpc.CrudBlockingStub crudBlockingStub;
    private final OperationalGrpc.OperationalBlockingStub operationalBlockingStub;

    public ServicesManagementClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9081).usePlaintext().build();
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
