package io.github.lorenzobucci.tesi.services_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "service_instance")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceInstance extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_type_id", nullable = false)
    protected ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "belonging_workflow_id")
    protected WorkflowInstance belongingWorkflow;

    @Embedded
    protected Container container;

    ServiceInstance(ServiceType serviceType, WorkflowInstance belongingWorkflow) {
        this.serviceType = serviceType;
        this.belongingWorkflow = belongingWorkflow;
        this.container = new Container(getServiceRequirements(), belongingWorkflow.getWorkflowRequirements());
    }

    protected ServiceInstance() {

    }

    void optimize() {
        container.optimize(getServiceRequirements(), belongingWorkflow.getWorkflowRequirements());
    }

    void onCompleted() {
        container.destroy();
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
        return "io.github.lorenzobucci.tesi.servicesmanagement.model.ServiceInstance{" +
                "uuid=" + getUuid() +
                ", serviceTypeID=" + serviceType.getId() +
                '}';
    }

}
