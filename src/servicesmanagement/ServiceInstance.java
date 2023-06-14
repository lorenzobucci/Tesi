package servicesmanagement;

import mobiledevice.UserRequirements;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceInstance {

    public final UUID id = UUID.randomUUID();

    public final ServiceType serviceType;
    private String serviceState = "IDLE";
    public final WorkflowInstance belongingWorkflow;

    public InetAddress nodeIpAddress;
    private Map<String, Integer> internalState;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = new ServiceType(serviceType);
        this.belongingWorkflow = belongingWorkflow;
    }

    public void syncWithRealObject(Map<String, Integer> internalState, String serviceState) {
        this.internalState = new HashMap<>(internalState);
        this.serviceState = serviceState;
    }

    public String getServiceState() {
        return serviceState;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceType.requirements;
    }

    public UserRequirements getUserRequirements() {
        return belongingWorkflow.getUserRequirements();
    }


    @Override
    public String toString() {
        return "ServiceInstance{" +
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
