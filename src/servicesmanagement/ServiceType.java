package servicesmanagement;

import java.util.Objects;

public class ServiceType {

    public final String name;

    public ServiceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceType{" + "name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceType that = (ServiceType) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
