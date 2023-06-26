import io.github.lorenzobucci.metamodel.servicesmanagement.ServiceInstance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class ContainerInstance {
    public final UUID id = UUID.randomUUID();

    public final ContainerType containerType;
    private String containerState = "IDLE";
    UUID belongingNodeId;

    public final ServiceInstance serviceInstance;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);
    private final Semaphore migrationSemaphore = new Semaphore(1);

    public ContainerInstance(ContainerType containerType, UUID belongingNodeId, ServiceInstance serviceInstance) {
        this.containerType = new ContainerType(containerType);
        this.belongingNodeId = belongingNodeId;
        this.serviceInstance = serviceInstance;
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

    public String getContainerState() {
        return containerState;
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
