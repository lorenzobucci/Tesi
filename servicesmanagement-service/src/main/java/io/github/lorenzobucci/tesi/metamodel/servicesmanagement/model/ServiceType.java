package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "service_type")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ServiceType {

    @Id
    protected UUID id = UUID.randomUUID();

    @Column(name = "service_name", nullable = false)
    protected String name;

    @Embedded
    protected ServiceRequirements requirements;

    public ServiceType(String name, ServiceRequirements requirements) {
        this.name = name;
        this.requirements = requirements;
    }

    protected ServiceType() {

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
        return "io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType{" +
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
