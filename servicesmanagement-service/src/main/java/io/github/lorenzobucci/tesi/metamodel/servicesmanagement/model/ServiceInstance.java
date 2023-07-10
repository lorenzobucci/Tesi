package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "service_instance")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceInstance {

    @Id
    protected UUID id = UUID.randomUUID();

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_type_id", nullable = false)
    protected ServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "belonging_workflow_id", nullable = false)
    protected WorkflowInstance belongingWorkflow;

    @Embedded
    protected Container container;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = serviceType;
        this.belongingWorkflow = belongingWorkflow;

        String dependabilityRequirements = getServiceRequirements().toString() + belongingWorkflow.getWorkflowRequirements().toString(); // MERGE SERVICE REQUIREMENTS + WORKFLOW REQUIREMENTS
        this.container = new Container(dependabilityRequirements);
    }

    protected ServiceInstance() {

    }

    public void optimize() {
        String dependabilityRequirements = getServiceRequirements().toString() + belongingWorkflow.getWorkflowRequirements().toString(); // MERGE SERVICE REQUIREMENTS + WORKFLOW REQUIREMENTS
        container.optimize(dependabilityRequirements);
    }

    public UUID getId() {
        return id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public WorkflowInstance getBelongingWorkflow() {
        return belongingWorkflow;
    }

    public ServiceRequirements getServiceRequirements() {
        return serviceType.getRequirements();
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public String toString() {
        return "io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceInstance{" +
                "id=" + id +
                ", serviceTypeID=" + serviceType.getId() +
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
