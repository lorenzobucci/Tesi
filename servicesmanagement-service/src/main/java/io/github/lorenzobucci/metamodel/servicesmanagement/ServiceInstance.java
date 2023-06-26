package io.github.lorenzobucci.metamodel.servicesmanagement;

import io.github.lorenzobucci.metamodel.resourcesmanagement.ContainerInstance;
import io.github.lorenzobucci.metamodel.resourcesmanagement.ServiceRequirements;
import io.github.lorenzobucci.metamodel.resourcesmanagement.UserRequirements;

import java.net.InetAddress;
import java.util.UUID;

public class ServiceInstance {

    public final UUID id = UUID.randomUUID();

    public final ServiceType serviceType;
    public final WorkflowInstance belongingWorkflow;

    ContainerInstance hostContainer;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = new ServiceType(serviceType);
        this.belongingWorkflow = belongingWorkflow;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceType.requirements;
    }

    public UserRequirements getUserRequirements() {
        return belongingWorkflow.getUserRequirements();
    }

    public InetAddress getIpAddress() {
        return hostContainer.getNodeIpAddress();
    }


    @Override
    public String toString() {
        return "io.github.lorenzobucci.metamodel.servicesmanagement.ServiceInstance{" +
                "id=" + id +
                ", serviceTypeID=" + serviceType.id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceInstance that = (ServiceInstance) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
