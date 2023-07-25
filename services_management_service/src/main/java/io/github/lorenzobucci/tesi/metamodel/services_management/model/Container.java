package io.github.lorenzobucci.tesi.metamodel.services_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.net.InetAddress;

@Embeddable
public class Container {

    @Column(name = "associated_container_id", unique = true)
    private long associatedContainerId;

    @Column(name = "container_ip_address")
    private InetAddress ipAddress;

    public Container(String dependabilityRequirements) {
        //(associatedContainerId, ipAddress) =AllocationManager.getInstance().allocateContainer(dependabilityRequirements); // TODO: ADJUST AND USE API
    }

    protected Container() {

    }

    void optimize(String dependabilityRequirements) {
        //ipAddress = AllocationManager.getInstance().reviseContainerAllocation(associatedContainerId, dependabilityRequirements); // TODO: ADJUST AND USE API
    }

    void destroy() {
        //AllocationManager.destroy; // TODO: ADJUST AND USE API
    }

    public long getAssociatedContainerId() {
        return associatedContainerId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }
}
