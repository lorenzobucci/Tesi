package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.net.URI;

@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(nullable = false)
    private URI endpoint;
    private String parameters;

    @Embedded
    private DependabilityRequirements requirements;

    @Column(name = "associated_workflow_id")
    private long associatedWorkflowId;

    Task(URI endpoint, String parameters, DependabilityRequirements requirements) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.requirements = requirements;
        //associatedWorkflowId = ServiceProxy.getInstance().requestService(this.toString()); // TODO: ADJUST AND USE API
    }

    protected Task() {

    }

    void updateRequirements(DependabilityRequirements requirements) {
        this.requirements = requirements;
        //ServiceProxy.getInstance().updateServiceRequirements(associatedTaskId, this.toString()); // TODO: ADJUST USE API
    }

    void onCompleted() {
        //ServiceProxy.getInstance().workflowcompleted; // TODO: ADJUST USE API
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

    public long getAssociatedWorkflowId() {
        return associatedWorkflowId;
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