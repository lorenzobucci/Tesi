package io.github.lorenzobucci.tesi.resources_management.controller;

import io.github.lorenzobucci.tesi.resources_management.dao.ContainerInstanceDao;
import io.github.lorenzobucci.tesi.resources_management.model.ContainerInstance;
import io.github.lorenzobucci.tesi.resources_management.model.Node;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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

    ContainerInstance getContainerInstance(UUID containerInstanceUuid) throws NoSuchElementException {
        try {
            return containerInstanceDao.findByUuid(containerInstanceUuid);
        } catch (NoResultException e) {
            throw new NoSuchElementException("ContainerInstance with uuid=" + containerInstanceUuid + " does not exist.");
        }
    }

    public List<ContainerInstance> retrieveContainerInstances() {
        return containerInstanceDao.findAll();
    }

    public void destroyContainerInstance(long containerInstanceId) throws NoSuchElementException {
        ContainerInstance containerInstance = getContainerInstance(containerInstanceId);
        Node node = nodeController.getNode(containerInstance.getBelongingNode().getId());
        node.removeOwnedContainer(containerInstance);
        nodeController.updateNode(node);
    }

    public void syncContainerDTProperties(long containerInstanceId, String containerState) throws NoSuchElementException {
        ContainerInstance containerInstance = getContainerInstance(containerInstanceId);
        containerInstance.syncWithRealObject(containerState);
        containerInstanceDao.update(containerInstance);
    }

}
