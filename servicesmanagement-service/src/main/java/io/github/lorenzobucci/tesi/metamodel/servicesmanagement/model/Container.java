package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.net.InetAddress;
import java.util.UUID;

@Embeddable
public class Container {

    @Column(name = "associated_container_id")
    private UUID associatedContainerId;


    @Column(name = "container_ip_address")
    private InetAddress ipAddress;

    public Container(UUID associatedContainerId, InetAddress ipAddress) {
        this.associatedContainerId = associatedContainerId;
        this.ipAddress = ipAddress;
    }

    protected Container() {

    }

    public UUID getAssociatedContainerId() {
        return associatedContainerId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }
}
