package wildfly.to_be_deleted_soon;

import io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.CrudGrpc;

/* !! THIS FAKE CLASS IS REQUIRED UNTIL WILDFLY SUPPORTS GRPC CLIENTS !!
DETAILS: from the definition of protobufs, useful code is automatically generated for both clients and server.
On the client side there are no implementations of the services defined in the self-generated code (the server
implements them), but Wildfly behavior is to instance any leaf BindableService class. Not finding them, an exception
is thrown because Wildfly try to instant the abstract self-generated superclasses *ImplBase. */

public class TBD1 extends CrudGrpc.CrudImplBase {

}
