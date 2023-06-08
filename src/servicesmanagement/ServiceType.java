package servicesmanagement;

import java.util.UUID;

public class ServiceType {

    public final UUID id;

    public final String name;

    public ServiceType(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    public ServiceType(ServiceType serviceType) {
        name = serviceType.name;
        id = serviceType.id;
    }

    @Override
    public String toString() {
        return "ServiceType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceType that = (ServiceType) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
