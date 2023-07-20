package io.github.lorenzobucci.tesi.metamodel.services_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "service_type")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceType extends BaseEntity {

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

    public String getName() {
        return name;
    }

    public ServiceRequirements getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return "io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType{" +
                "uuid=" + getUuid() +
                ", name='" + name + '\'' +
                '}';
    }

}
