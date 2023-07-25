package io.github.lorenzobucci.tesi.metamodel.resources_management.model;

import jakarta.persistence.*;

import java.net.InetAddress;

@Entity
@Table(name = "container_instance")
public class ContainerInstance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "container_type_id", nullable = false)
    private ContainerType containerType;

    @Column(name = "container_state", nullable = false)
    private String containerState = "IDLE";

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(name = "node_container_instances",
            joinColumns = {@JoinColumn(name = "container_instance_id", insertable = false,
                    updatable = false, referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "node_id", insertable = false,
                    updatable = false, referencedColumnName = "id")})
    private Node belongingNode;

    public ContainerInstance(ContainerType containerType, Node belongingNode) {
        this.containerType = containerType;
        this.belongingNode = belongingNode;
    }

    protected ContainerInstance() {

    }

    public void syncWithRealObject(String containerState) {
        this.containerState = containerState;
    }

    public void updateBelongingNode(Node newBelongingNode) {
        belongingNode = newBelongingNode;
    }

    public String getContainerState() {
        return containerState;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public Node getBelongingNode() {
        return belongingNode;
    }

    public InetAddress getNodeIpAddress() {
        return belongingNode.getIpAddress();
    }

}
