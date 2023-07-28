package io.github.lorenzobucci.tesi.metamodel.resources_management.controller;

import io.github.lorenzobucci.tesi.metamodel.resources_management.dao.ContainerTypeDao;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerType;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class ContainerTypeController {

    @Inject
    private ContainerTypeDao containerTypeDao;

    public ContainerType addContainerType(String imageName, String imageVersion) {
        ContainerType containerType = new ContainerType(imageName, imageVersion);
        containerTypeDao.create(containerType);
        return containerTypeDao.findByUuid(containerType.getUuid());
    }

    public ContainerType getContainerType(long containerTypeId) throws NoSuchElementException {
        ContainerType containerType = containerTypeDao.findById(containerTypeId);
        if (containerType != null)
            return containerType;
        else
            throw new NoSuchElementException("ContainerType with id=" + containerTypeId + " does not exist.");
    }

    public List<ContainerType> retrieveContainerTypes() {
        return containerTypeDao.findAll();
    }

    public void removeContainerType(long containerTypeId) throws NoSuchElementException {
        containerTypeDao.delete(getContainerType(containerTypeId));
    }

}
