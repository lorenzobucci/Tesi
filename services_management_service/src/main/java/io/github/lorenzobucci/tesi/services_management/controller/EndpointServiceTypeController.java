package io.github.lorenzobucci.tesi.services_management.controller;

import io.github.lorenzobucci.tesi.services_management.dao.EndpointServiceTypeDao;
import io.github.lorenzobucci.tesi.services_management.model.EndpointServiceType;
import io.github.lorenzobucci.tesi.services_management.model.ServiceRequirements;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class EndpointServiceTypeController {

    @Inject
    private EndpointServiceTypeDao endpointServiceTypeDao;

    public EndpointServiceType addEndpointServiceType(String serviceName, ServiceRequirements serviceRequirements, String physicalEndpointURI) {
        EndpointServiceType endpointServiceType = new EndpointServiceType(serviceName, serviceRequirements, physicalEndpointURI);
        endpointServiceTypeDao.create(endpointServiceType);
        return endpointServiceTypeDao.findByUuid(endpointServiceType.getUuid());
    }

    public EndpointServiceType getEndpointServiceType(long endpointServiceTypeId) throws NoSuchElementException {
        EndpointServiceType endpointServiceType = endpointServiceTypeDao.findById(endpointServiceTypeId);
        if (endpointServiceType != null)
            return endpointServiceType;
        else
            throw new NoSuchElementException("EndpointServiceType with id=" + endpointServiceTypeId + " does not exist.");
    }

    public EndpointServiceType getEndpointServiceType(String endpointURI) throws NoSuchElementException {
        EndpointServiceType endpointServiceType = endpointServiceTypeDao.findByURI(endpointURI);
        if (endpointServiceType != null)
            return endpointServiceType;
        else
            throw new NoSuchElementException("EndpointServiceType with URI=" + endpointURI + " does not exist.");
    }

    public List<EndpointServiceType> retrieveEndpointServiceTypes() {
        return endpointServiceTypeDao.findAll();
    }

    public void removeEndpointServiceType(long endpointServiceTypeId) throws NoSuchElementException {
        endpointServiceTypeDao.delete(getEndpointServiceType(endpointServiceTypeId));
    }
}
