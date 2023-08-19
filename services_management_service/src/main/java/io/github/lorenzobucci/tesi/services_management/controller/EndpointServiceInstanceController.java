package io.github.lorenzobucci.tesi.services_management.controller;

import io.github.lorenzobucci.tesi.services_management.dao.EndpointServiceInstanceDao;
import io.github.lorenzobucci.tesi.services_management.model.EndpointServiceInstance;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class EndpointServiceInstanceController {

    @Inject
    private EndpointServiceInstanceDao endpointServiceInstanceDao;

    public EndpointServiceInstance getEndpointServiceInstance(long endpointServiceInstanceId) throws NoSuchElementException {
        EndpointServiceInstance endpointServiceInstance = endpointServiceInstanceDao.findById(endpointServiceInstanceId);
        if (endpointServiceInstance != null)
            return endpointServiceInstance;
        else
            throw new NoSuchElementException("EndpointServiceInstance with id=" + endpointServiceInstanceId + " does not exist.");
    }

    public List<EndpointServiceInstance> retrieveEndpointServiceInstances() {
        return endpointServiceInstanceDao.findAll();
    }
}
