package io.github.lorenzobucci.tesi.metamodel.resources_management.controller;

import io.github.lorenzobucci.tesi.metamodel.resources_management.dao.ContainerInstanceDao;
import io.github.lorenzobucci.tesi.metamodel.resources_management.model.ContainerInstance;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class ContainerInstanceController {

    @Inject
    private ContainerInstanceDao containerInstanceDao;

    @Inject
    private NodeController nodeController;

    public ContainerInstance getContainerInstance(long containerInstanceId) throws NoSuchElementException {
        ContainerInstance containerInstance = containerInstanceDao.findById(containerInstanceId);
        if (containerInstance != null)
            return containerInstance;
        else
            throw new NoSuchElementException("ContainerInstance with id=" + containerInstanceId + " does not exist.");
    }

    public List<ContainerInstance> retrieveContainerInstances() {
        return containerInstanceDao.findAll();
    }

    public void destroyContainerInstance(long containerInstanceId) throws NoSuchElementException {
        ContainerInstance containerInstance = getContainerInstance(containerInstanceId);
        containerInstance.getBelongingNode().removeOwnedContainer(containerInstance);
        nodeController.updateNode(containerInstance.getBelongingNode());
        containerInstanceDao.delete(containerInstance);
    }

    ContainerInstance addContainerInstance(ContainerInstance containerInstance) {
        containerInstanceDao.create(containerInstance);
        return containerInstanceDao.findByUuid(containerInstance.getUuid());
    }
}
