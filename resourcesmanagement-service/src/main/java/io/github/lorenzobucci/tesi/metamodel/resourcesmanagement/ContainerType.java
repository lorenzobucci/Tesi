package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "container_type")
public class ContainerType {

    @Id
    private UUID id = UUID.randomUUID();

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

    public UUID getId() {
        return id;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerType that = (ContainerType) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
