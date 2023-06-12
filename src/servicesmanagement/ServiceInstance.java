package servicesmanagement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceInstance {

    public final UUID id = UUID.randomUUID();

    public final ServiceType serviceType;
    private Map<String, Integer> internalState;

    ServiceInstance(ServiceType serviceType) {
        this.serviceType = new ServiceType(serviceType);
    }

    public void syncInternalState(Map<String, Integer> internalState) {
        this.internalState = new HashMap<>(internalState);
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
