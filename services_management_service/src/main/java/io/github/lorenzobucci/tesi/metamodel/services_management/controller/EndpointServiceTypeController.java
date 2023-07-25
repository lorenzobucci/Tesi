package io.github.lorenzobucci.tesi.metamodel.services_management.controller;

import io.github.lorenzobucci.tesi.metamodel.services_management.dao.EndpointServiceTypeDao;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.EndpointServiceType;
import io.github.lorenzobucci.tesi.metamodel.services_management.model.ServiceRequirements;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

public class EndpointServiceTypeController {

    @Inject
    private EndpointServiceTypeDao endpointServiceTypeDao;

    public EndpointServiceType addEndpointServiceType(String serviceName, ServiceRequirements serviceRequirements, URI physicalEndpointURI) {
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

    public EndpointServiceType getEndpointServiceType(URI endpointURI) throws NoSuchElementException {
        EndpointServiceType endpointServiceType = endpointServiceTypeDao.findByURI(endpointURI);
        if (endpointServiceType != null)
            return endpointServiceType;
        else
            throw new NoSuchElementException("EndpointServiceType with URI=" + endpointURI + " does not exist.");
    }

    public List<EndpointServiceType> retrieveEndpointServiceTypes() {
        return endpointServiceTypeDao.findAll();
    }

    public void removeEndpointServiceType(long endpointServiceTypeId) throws NoSuchElementException, ConstraintViolationException {
        endpointServiceTypeDao.delete(getEndpointServiceType(endpointServiceTypeId));
    }
}
