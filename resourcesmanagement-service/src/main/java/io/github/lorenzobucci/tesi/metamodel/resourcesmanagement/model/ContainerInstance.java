package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model;

import jakarta.persistence.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

@Entity
@Table(name = "container_instance")
public class ContainerInstance extends BaseEntity {

    @Transient
    private final Semaphore migrationSemaphore = new Semaphore(1);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "container_type_id", nullable = false)
    private ContainerType containerType;

    @Column(name = "container_state", nullable = false)
    private String containerState = "IDLE";

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(name = "node_container_instances",
            joinColumns = {@JoinColumn(name = "container_instance_id", insertable = false,
                    updatable = false, referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "node_id", insertable = false,
                    updatable = false, referencedColumnName = "id")}
    )
    private Node belongingNode;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    public ContainerInstance(ContainerType containerType, Node belongingNode) {
        this.containerType = containerType;
        this.belongingNode = belongingNode;
    }

    protected ContainerInstance() {

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        eventSupport.removePropertyChangeListener(pcl);
    }

    public void syncWithRealObject(String containerState) {
        if (migrationSemaphore.tryAcquire()) {
            this.containerState = containerState; // NO NOTIFY
            migrationSemaphore.release();
        } else
            throw new IllegalStateException("The container is being migrated, no changes possible.");
    }

    public void acquireMigrationSemaphore() throws InterruptedException {
        migrationSemaphore.acquire();
    }

    public void releaseMigrationSemaphore() {
        migrationSemaphore.release();
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

    public void setBelongingNode(Node belongingNode) {
        this.belongingNode = belongingNode;
    }

    public InetAddress getNodeIpAddress() {
        return belongingNode.getIpAddress();
    }

    public void setContainerState(String containerState) {
        eventSupport.firePropertyChange("containerState", this.containerState, containerState);
        this.containerState = containerState;
    }

}
