package io.github.lorenzobucci.tesi.services_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "endpoint_service_type")
public class EndpointServiceType extends ServiceType {

    @Column(name = "physical_endpoint_uri", nullable = false, unique = true)
    private String physicalEndpointURI;

    public EndpointServiceType(String name, ServiceRequirements requirements, String physicalEndpointURI) {
        super(name, requirements);
        this.physicalEndpointURI = physicalEndpointURI;
    }

    protected EndpointServiceType() {

    }

    public String getPhysicalEndpointURI() {
        return physicalEndpointURI;
    }

}
