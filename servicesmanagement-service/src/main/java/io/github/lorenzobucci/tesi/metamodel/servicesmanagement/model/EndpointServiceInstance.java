package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "endpoint_service_instance")
public class EndpointServiceInstance extends ServiceInstance {

    @ManyToOne(optional = false)
    @JoinColumn(name = "endpoint_service_type_id", nullable = false)
    private EndpointServiceType endpointServiceType;

    @Column(name = "endpoint_parameters")
    private String parameters;

    public EndpointServiceInstance(EndpointServiceType endpointServiceType, WorkflowInstance belongingWorkflow, String endpointParameters) {
        super(endpointServiceType, belongingWorkflow);
        this.endpointServiceType = endpointServiceType;
        this.parameters = endpointParameters;
    }

    protected EndpointServiceInstance() {

    }

    public EndpointServiceType getEndpointServiceType() {
        return endpointServiceType;
    }

    public String getParameters() {
        return parameters;
    }
}
