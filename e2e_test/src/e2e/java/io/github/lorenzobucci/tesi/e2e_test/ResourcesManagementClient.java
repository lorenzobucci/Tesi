package io.github.lorenzobucci.tesi.e2e_test;

import io.github.lorenzobucci.tesi.resources_management.service.gRPC.CrudGrpc;
import io.github.lorenzobucci.tesi.resources_management.service.gRPC.OperationalGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class ResourcesManagementClient {

    ManagedChannel channel;

    private final CrudGrpc.CrudBlockingStub crudBlockingStub;
    private final OperationalGrpc.OperationalBlockingStub operationalBlockingStub;

    public ResourcesManagementClient() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9082).usePlaintext().build();
        crudBlockingStub = CrudGrpc.newBlockingStub(channel);
        operationalBlockingStub = OperationalGrpc.newBlockingStub(channel);
    }

    public CrudGrpc.CrudBlockingStub getCrudBlockingStub() {
        return crudBlockingStub;
    }

    public OperationalGrpc.OperationalBlockingStub getOperationalBlockingStub() {
        return operationalBlockingStub;
    }

    public void closeChannel() {
        channel.shutdown();
        try {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

