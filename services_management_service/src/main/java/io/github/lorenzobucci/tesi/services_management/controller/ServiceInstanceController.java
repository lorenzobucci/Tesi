package io.github.lorenzobucci.tesi.services_management.controller;

import io.github.lorenzobucci.tesi.services_management.dao.ServiceInstanceDao;
import io.github.lorenzobucci.tesi.services_management.model.ServiceInstance;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class ServiceInstanceController {

    @Inject
    private ServiceInstanceDao serviceInstanceDao;

    public ServiceInstance getServiceInstance(long serviceInstanceId) throws NoSuchElementException {
        ServiceInstance serviceInstance = serviceInstanceDao.findById(serviceInstanceId);
        if (serviceInstance != null)
            return serviceInstance;
        else
            throw new NoSuchElementException("ServiceInstance with id=" + serviceInstanceId + " does not exist.");
    }

    public List<ServiceInstance> retrieveServiceInstances() {
        return serviceInstanceDao.findAll();
    }
}
