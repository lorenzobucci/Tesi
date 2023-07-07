package io.github.lorenzobucci.tesi.metamodel.mobiledevice.model;

import jakarta.persistence.*;

import java.net.URI;
import java.util.UUID;

@Entity(name = "task")
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private URI endpoint;
    private String parameters;

    @Embedded
    private DependabilityRequirements requirements;

    public Task(URI endpoint, String parameters, DependabilityRequirements requirements) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.requirements = requirements;
        ServiceProxy.getInstance().requestService(this.toString()); // TODO: ADJUST AND USE API
    }

    protected Task() {

    }

    public URI getEndpoint() {
        return endpoint;
    }

    public String getParameters() {
        return parameters;
    }

    public DependabilityRequirements getRequirements() {
        return requirements;
    }

    public void updateRequirements(DependabilityRequirements requirements) {
        this.requirements = requirements;
        ServiceProxy.getInstance().updateServiceRequirements(this.toString()); // TODO: ADJUST USE API
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return endpoint.equals(task.endpoint);
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }
}
