package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.URI;
import java.util.UUID;

public class ServiceInstance {

    public final UUID id = UUID.randomUUID();

    public final ServiceType serviceType;
    public final WorkflowInstance belongingWorkflow;

    public URI baseUri;

    JSONObject serviceParameters;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = new ServiceType(serviceType);
        this.belongingWorkflow = belongingWorkflow;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceType.requirements;
    }

    void determineBaseUri(InetAddress containerIp) {
        // PROTOCOL AND PORT ARE DEFINED ON THE BASIS OF THE REQUIREMENTS AND OTHER VARIABLES
        baseUri = URI.create("http://" + containerIp.getHostName() + ":8080");
    }

    @Override
    public String toString() {
        return "io.github.lorenzobucci.metamodel.servicesmanagement.ServiceInstance{" +
                "id=" + id +
                ", serviceTypeID=" + serviceType.id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceInstance that = (ServiceInstance) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
