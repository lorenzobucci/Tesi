package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class ContainerInstance {
    private final UUID id = UUID.randomUUID();

    private final ContainerType containerType;
    private String containerState = "IDLE";
    private UUID belongingNodeId;
    private InetAddress nodeIpAddress;

    private UUID associatedServiceId;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);
    private final Semaphore migrationSemaphore = new Semaphore(1);

    public ContainerInstance(ContainerType containerType, UUID belongingNodeId, InetAddress nodeIpAddress) {
        this.containerType = new ContainerType(containerType);
        this.belongingNodeId = belongingNodeId;
        this.nodeIpAddress = nodeIpAddress;
    }

    void addPropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.addPropertyChangeListener(pcl);
    }

    void removePropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.removePropertyChangeListener(pcl);
    }

    void syncWithRealObject(String containerState) {
        if (migrationSemaphore.tryAcquire()) {
            this.containerState = containerState; // NO NOTIFY
            migrationSemaphore.release();
        } else
            throw new IllegalStateException("The container is being migrated, no changes possible.");
    }

    void acquireMigrationSemaphore() throws InterruptedException {
        migrationSemaphore.acquire();
    }

    void releaseMigrationSemaphore() {
        migrationSemaphore.release();
    }

    public UUID getId() {
        return id;
    }

    public String getContainerState() {
        return containerState;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public UUID getBelongingNodeId() {
        return belongingNodeId;
    }

    void setBelongingNodeId(UUID belongingNodeId) {
        this.belongingNodeId = belongingNodeId;
    }

    public InetAddress getNodeIpAddress() {
        return nodeIpAddress;
    }

    void setNodeIpAddress(InetAddress nodeIpAddress) {
        this.nodeIpAddress = nodeIpAddress;
    }

    public UUID getAssociatedServiceId() {
        return associatedServiceId;
    }

    void setAssociatedServiceId(UUID associatedServiceId) {
        this.associatedServiceId = associatedServiceId;
    }

    void setContainerState(String containerState) {
        eventSupport.firePropertyChange("containerState", this.containerState, containerState);
        this.containerState = containerState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerInstance that = (ContainerInstance) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
