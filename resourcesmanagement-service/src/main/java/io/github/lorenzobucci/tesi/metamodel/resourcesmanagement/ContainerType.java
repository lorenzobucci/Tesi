package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement;

import java.util.Objects;
import java.util.UUID;

public class ContainerType {
    private final UUID id;
    private final String imageName;
    private final String imageVersion;

    public ContainerType(String imageName, String imageVersion) {
        id = UUID.randomUUID();
        this.imageName = imageName;
        this.imageVersion = imageVersion;
    }

    public ContainerType(ContainerType containerType) {
        id = containerType.id;
        imageName = containerType.imageName;
        imageVersion = containerType.imageVersion;
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
