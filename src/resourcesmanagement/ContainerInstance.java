package resourcesmanagement;

import mobiledevice.UserRequirements;
import servicesmanagement.ServiceInstance;
import servicesmanagement.ServiceRequirements;

import java.util.UUID;

public class ContainerInstance {
    public final UUID id = UUID.randomUUID();

    public final ContainerType containerType;
    public String state = "IDLE";
    public UUID belongingNodeId;

    public final ServiceInstance serviceInstance;

    public ContainerInstance(ContainerType containerType, UUID belongingNodeId, ServiceInstance serviceInstance) {
        this.containerType = new ContainerType(containerType);
        this.belongingNodeId = belongingNodeId;
        this.serviceInstance = serviceInstance;
    }

    public void syncWithRealObject(String state) {
        this.state = state;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceInstance.serviceType.requirements;
    }

    public UserRequirements getUserRequirements() {
        return serviceInstance.belongingWorkflow.userRequirements;
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
