package io.github.lorenzobucci.tesi.metamodel.mobile_device.controller;

import io.github.lorenzobucci.tesi.metamodel.mobile_device.dao.MobileDeviceDTDao;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.MobileDeviceDT;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.Position;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.model.TrajectoryForecaster;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

public class MobileDeviceDTController {

    @Inject
    private MobileDeviceDTDao mobileDeviceDTDao;

    @Inject
    private TaskController taskController;

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

    public void signalMobileDeviceTaskCompletion(long mobileDeviceDTId, long taskId) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.taskCompleted(taskController.getTask(taskId));
        mobileDeviceDTDao.update(mobileDeviceDT);
    }

    public void syncMobileDeviceDTProperties(long mobileDeviceDTId, Position currentPosition) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.syncWithRealObject(currentPosition);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }

    public void setMobileDeviceDTTrajectoryForecaster(long mobileDeviceDTId, TrajectoryForecaster trajectoryForecaster) throws NoSuchElementException {
        MobileDeviceDT mobileDeviceDT = getMobileDeviceDT(mobileDeviceDTId);
        mobileDeviceDT.setTrajectoryForecaster(trajectoryForecaster);
        mobileDeviceDTDao.update(mobileDeviceDT);
    }


}
