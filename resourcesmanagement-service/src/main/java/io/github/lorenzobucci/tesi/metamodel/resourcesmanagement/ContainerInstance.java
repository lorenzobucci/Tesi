package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import jakarta.persistence.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@Entity(name = "container_instance")
public class ContainerInstance {

    @Transient
    private final Semaphore migrationSemaphore = new Semaphore(1);

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(optional = false)
    @JoinColumn(name = "container_type_id", nullable = false)
    private ContainerType containerType;

    @Column(name = "container_state", nullable = false)
    private String containerState = "IDLE";

    @ManyToOne()
    @JoinColumn(name = "belonging_node_id")
    private Node belongingNode;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    @Column(name = "associated_service_id")
    private UUID associatedServiceId;

    public ContainerInstance(ContainerType containerType, Node belongingNode) {
        this.containerType = containerType;
        this.belongingNode = belongingNode;
    }

    protected ContainerInstance() {

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

    public Node getBelongingNode() {
        return belongingNode;
    }

    void setBelongingNode(Node belongingNode) {
        this.belongingNode = belongingNode;
    }

    public InetAddress getNodeIpAddress() {
        return belongingNode.getIpAddress();
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
