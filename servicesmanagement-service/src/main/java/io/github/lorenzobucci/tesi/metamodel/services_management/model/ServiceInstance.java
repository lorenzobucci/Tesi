package io.github.lorenzobucci.tesi.metamodel.services_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "service_instance")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceInstance extends BaseEntity {

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

    void optimize() {
        String dependabilityRequirements = getServiceRequirements().toString() + belongingWorkflow.getWorkflowRequirements().toString(); // MERGE SERVICE REQUIREMENTS + WORKFLOW REQUIREMENTS
        container.optimize(dependabilityRequirements);
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
        return "io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceInstance{" +
                "uuid=" + getUuid() +
                ", serviceTypeID=" + serviceType.getId() +
                '}';
    }

}
