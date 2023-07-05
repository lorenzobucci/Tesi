package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import jakarta.persistence.*;

import java.net.InetAddress;
import java.net.URI;
import java.util.UUID;

@Entity
@Table(name = "service_instance")
public class ServiceInstance {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "belonging_workflow_id", nullable = false)
    private WorkflowInstance belongingWorkflow;

    @Column(name = "base_uri")
    private URI baseUri;

    @Column(name = "service_parameters")
    private String serviceParameters;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = serviceType;
        this.belongingWorkflow = belongingWorkflow;
    }

    protected ServiceInstance() {

    }

    void determineBaseUri(InetAddress containerIp) {
        // PROTOCOL AND PORT ARE DEFINED ON THE BASIS OF THE REQUIREMENTS AND OTHER VARIABLES
        baseUri = URI.create("http://" + containerIp.getHostName() + ":8080");
    }

    public UUID getId() {
        return id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public WorkflowInstance getBelongingWorkflow() {
        return belongingWorkflow;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public String getServiceParameters() {
        return serviceParameters;
    }

    void setServiceParameters(String serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceType.getRequirements();
    }

    @Override
    public String toString() {
        return "io.github.lorenzobucci.tesi.metamodel.servicesmanagement.ServiceInstance{" +
                "id=" + id +
                ", serviceTypeID=" + serviceType.getId() +
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
