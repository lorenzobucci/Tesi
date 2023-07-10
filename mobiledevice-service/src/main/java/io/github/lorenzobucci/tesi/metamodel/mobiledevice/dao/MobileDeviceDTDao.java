package io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao;

import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.MobileDeviceDT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class MobileDeviceDTDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(MobileDeviceDT newMobileDevice) {
        em.persist(newMobileDevice);
    }

    @Transactional
    public void delete(MobileDeviceDT mobileDevice) {
        em.remove(mobileDevice);
    }

    @Transactional
    public void update(MobileDeviceDT mobileDevice) {
        em.merge(mobileDevice);
    }

    public List<MobileDeviceDT> findAll() {
        TypedQuery<MobileDeviceDT> query = em.createQuery("from MobileDeviceDT ", MobileDeviceDT.class);
        return query.getResultList();
    }

    public MobileDeviceDT findById(UUID mobileDeviceId) {
        return em.find(MobileDeviceDT.class, mobileDeviceId);
    }

}
