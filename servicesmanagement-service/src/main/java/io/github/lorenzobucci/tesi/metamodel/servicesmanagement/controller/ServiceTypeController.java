package io.github.lorenzobucci.tesi.metamodel.servicesmanagement.controller;

import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.dao.ServiceTypeDao;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceRequirements;
import io.github.lorenzobucci.tesi.metamodel.servicesmanagement.model.ServiceType;
import jakarta.inject.Inject;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.NoSuchElementException;

public class ServiceTypeController {

    @Inject
    private ServiceTypeDao serviceTypeDao;

    public ServiceType addServiceType(String serviceName, ServiceRequirements serviceRequirements) {
        ServiceType serviceType = new ServiceType(serviceName, serviceRequirements);
        serviceTypeDao.create(serviceType);
        return serviceTypeDao.findByUuid(serviceType.getUuid());
    }

    public ServiceType getServiceType(long serviceTypeId) throws NoSuchElementException {
        ServiceType serviceType = serviceTypeDao.findById(serviceTypeId);
        if (serviceType != null)
            return serviceType;
        else
            throw new NoSuchElementException("ServiceType with id=" + serviceTypeId + " does not exist.");
    }

    public List<ServiceType> retrieveServiceTypes() {
        return serviceTypeDao.findAll();
    }

    public void removeServiceType(long serviceTypeId) throws NoSuchElementException, ConstraintViolationException {
        serviceTypeDao.delete(getServiceType(serviceTypeId));
    }
}
