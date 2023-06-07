package servicesmanagement;

import java.util.HashMap;
import java.util.Objects;

public class ServiceInstance {

    final ServiceType serviceType;
    private HashMap<String, Integer> internalState;

    public ServiceInstance(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void updateInternalState(HashMap<String, Integer> internalState) {
        this.internalState = new HashMap<>(internalState);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceType=" + serviceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceInstance that = (ServiceInstance) o;

        if (!Objects.equals(serviceType, that.serviceType)) return false;
        return Objects.equals(internalState, that.internalState);
    }

    @Override
    public int hashCode() {
        int result = serviceType != null ? serviceType.hashCode() : 0;
        result = 31 * result + (internalState != null ? internalState.hashCode() : 0);
        return result;
    }
}
