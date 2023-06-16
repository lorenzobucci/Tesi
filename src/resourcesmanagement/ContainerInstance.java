package resourcesmanagement;

import servicesmanagement.ServiceInstance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

public class ContainerInstance {
    public final UUID id = UUID.randomUUID();

    public final ContainerType containerType;
    private String containerState = "IDLE";
    UUID belongingNodeId;

    public final ServiceInstance serviceInstance;

    private final PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

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
        this.containerState = containerState; // NO NOTIFY
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
