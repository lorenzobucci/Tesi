package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "container_type")
public class ContainerType extends BaseEntity {

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "image_version", nullable = false)
    private String imageVersion;

    public ContainerType(String imageName, String imageVersion) {
        this.imageName = imageName;
        this.imageVersion = imageVersion;
    }

    protected ContainerType() {

    }

    public String getImageName() {
        return imageName;
    }

    public String getImageVersion() {
        return imageVersion;
    }

}
