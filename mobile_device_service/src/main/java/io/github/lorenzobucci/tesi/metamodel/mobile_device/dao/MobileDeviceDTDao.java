package io.github.lorenzobucci.tesi.metamodel.mobile_device.dao;

import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.MobileDeviceDT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class MobileDeviceDTDao {

    @PersistenceContext
    private EntityManager em;

    public void create(MobileDeviceDT newMobileDevice) {
        em.persist(newMobileDevice);
    }

    public void delete(MobileDeviceDT mobileDevice) {
        em.remove(mobileDevice);
    }

    public void update(MobileDeviceDT mobileDevice) {
        em.merge(mobileDevice);
    }

    public List<MobileDeviceDT> findAll() {
        TypedQuery<MobileDeviceDT> query = em.createQuery("select p from MobileDeviceDT p", MobileDeviceDT.class);
        return query.getResultList();
    }

    public MobileDeviceDT findById(long mobileDeviceId) {
        return em.find(MobileDeviceDT.class, mobileDeviceId);
    }

    public MobileDeviceDT findByUuid(UUID mobileDeviceUuid) {
        TypedQuery<MobileDeviceDT> query =
                em.createQuery("select p from MobileDeviceDT p where p.uuid = :mobileDeviceUuid", MobileDeviceDT.class)
                        .setParameter("mobileDeviceUuid", mobileDeviceUuid);
        return query.getSingleResult();
    }

}
