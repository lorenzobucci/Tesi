package io.github.lorenzobucci.tesi.metamodel.mobiledevice.controller;

import io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao.MobileDeviceDTDao;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Position;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;

public class MobileDeviceDTController {

    @Inject
    private MobileDeviceDTDao mobileDeviceDTDao;

    public long addMobileDeviceDT() {
        MobileDeviceDT mobileDeviceDT = new MobileDeviceDT();
        mobileDeviceDTDao.create(mobileDeviceDT);
        return mobileDeviceDTDao.findByUuid(mobileDeviceDT.getUuid()).getId();
    }

    public MobileDeviceDT getMobileDeviceDT(long mobileDeviceDTId) {
        return mobileDeviceDTDao.findById(mobileDeviceDTId);
    }

    public List<MobileDeviceDT> retrieveMobileDeviceDTs() {
        return mobileDeviceDTDao.findAll();
    }

    public void removeMobileDeviceDT(long mobileDeviceDTId) {
        mobileDeviceDTDao.delete(getMobileDeviceDT(mobileDeviceDTId));
    }

    public void signalMobileDeviceEndpointInvocation(long mobileDeviceDTId, URI invokedEndpoint, String sentParameters) {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.taskInvoked(invokedEndpoint, sentParameters);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }

    public void syncMobileDeviceDTProperties(long mobileDeviceDTId, Position currentPosition) {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.syncWithRealObject(currentPosition);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }


}
