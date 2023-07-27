package io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC;


import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ResourcesManagementServer {

    private final Server server;

    public ResourcesManagementServer(int port) {
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new CrudService())
                .addService(new OperationalService())
                .build();
    }

    public static void main(String[] args) throws Exception {
        ResourcesManagementServer server = new ResourcesManagementServer(8080);
        server.start();
        server.blockUntilShutdown();
    }

    public void start() throws IOException {
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ResourcesManagementServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
