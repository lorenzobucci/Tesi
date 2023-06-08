package servicesmanagement;

import java.util.HashMap;
import java.util.UUID;

public class ServiceInstance {

    public final UUID id = UUID.randomUUID();

    public final UUID serviceTypeId;
    private HashMap<String, Integer> internalState;

    public ServiceInstance(ServiceType serviceType) {
        serviceTypeId = serviceType.id;
    }

    public void updateInternalState(HashMap<String, Integer> internalState) {
        this.internalState = new HashMap<>(internalState);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "id=" + id +
                ", serviceTypeID=" + serviceTypeId +
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
