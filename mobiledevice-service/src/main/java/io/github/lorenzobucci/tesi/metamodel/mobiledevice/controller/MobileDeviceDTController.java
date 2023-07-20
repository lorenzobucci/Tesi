package io.github.lorenzobucci.tesi.metamodel.mobiledevice.controller;

import io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao.MobileDeviceDTDao;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Position;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

public class MobileDeviceDTController {

    @Inject
    private MobileDeviceDTDao mobileDeviceDTDao;

    public MobileDeviceDT addMobileDeviceDT() {
        MobileDeviceDT mobileDeviceDT = new MobileDeviceDT();
        mobileDeviceDTDao.create(mobileDeviceDT);
        return mobileDeviceDTDao.findByUuid(mobileDeviceDT.getUuid());
    }

    public MobileDeviceDT getMobileDeviceDT(long mobileDeviceDTId) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = mobileDeviceDTDao.findById(mobileDeviceDTId);
        if (mobileDeviceDT != null)
            return mobileDeviceDT;
        else
            throw new NoSuchElementException("MobileDeviceDT with id=" + mobileDeviceDTId + " does not exist.");
    }

    public List<MobileDeviceDT> retrieveMobileDeviceDTs() {
        return mobileDeviceDTDao.findAll();
    }

    public void removeMobileDeviceDT(long mobileDeviceDTId) throws NoSuchElementException {
        mobileDeviceDTDao.delete(getMobileDeviceDT(mobileDeviceDTId));
    }

    public void signalMobileDeviceEndpointInvocation(long mobileDeviceDTId, URI invokedEndpoint, String sentParameters) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.taskInvoked(invokedEndpoint, sentParameters);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }

    public void syncMobileDeviceDTProperties(long mobileDeviceDTId, Position currentPosition) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.syncWithRealObject(currentPosition);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }


}
