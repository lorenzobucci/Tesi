package io.github.lorenzobucci.tesi.metamodel.servicesmanagement;

import java.util.UUID;

public class ServiceType {

    private final UUID id;

    private final String name;
    private final ServiceRequirements requirements;

    public ServiceType(String name, ServiceRequirements requirements) {
        this.name = name;
        id = UUID.randomUUID();
        this.requirements = requirements;
    }

    public ServiceType(ServiceType serviceType) {
        name = serviceType.name;
        id = serviceType.id;
        requirements = serviceType.requirements;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServiceRequirements getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return "io.github.lorenzobucci.metamodel.servicesmanagement.ServiceType{" +
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
