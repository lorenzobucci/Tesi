package io.github.lorenzobucci.tesi.metamodel.services_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.net.URI;

@Entity
@Table(name = "endpoint_service_type")
public class EndpointServiceType extends ServiceType {

    @Column(name = "physical_endpoint_uri", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private URI physicalEndpointURI;

    public EndpointServiceType(String name, ServiceRequirements requirements, URI physicalEndpointURI) {
        super(name, requirements);
        this.physicalEndpointURI = physicalEndpointURI;
    }

    protected EndpointServiceType() {

    }

    public URI getPhysicalEndpointURI() {
        return physicalEndpointURI;
    }
}
