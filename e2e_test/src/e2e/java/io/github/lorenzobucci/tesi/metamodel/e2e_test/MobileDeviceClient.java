package io.github.lorenzobucci.tesi.metamodel.e2e_test;

import io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.CrudGrpc;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.OperationalGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MobileDeviceClient {

    private final CrudGrpc.CrudBlockingStub crudBlockingStub;
    private final OperationalGrpc.OperationalBlockingStub operationalBlockingStub;

    public MobileDeviceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9080).usePlaintext().build();
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
