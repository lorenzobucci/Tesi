package io.github.lorenzobucci.tesi.metamodel.mobiledevice;


import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.MobileDeviceDT;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MobileDeviceManager {
    private static MobileDeviceManager instance = null;

    private final Map<UUID, MobileDeviceDT> mobileDevices = new HashMap<>();

    private MobileDeviceManager() {
    }

    public static MobileDeviceManager getInstance() {
        if (instance == null)
            instance = new MobileDeviceManager();
        return instance;
    }

    public void init(Set<MobileDeviceDT> mobileDeviceDTS) {
        if (this.mobileDevices.isEmpty()) {
            for (MobileDeviceDT mobileDeviceDT : mobileDeviceDTS)
                addMobileDevice(mobileDeviceDT);
        } else
            throw new IllegalStateException("Cannot initialize if there are mobile devices in memory.");
    }

    public void addMobileDevice(MobileDeviceDT mobileDeviceDT) {
        if (!mobileDevices.containsKey(mobileDeviceDT.getId()))
            mobileDevices.putIfAbsent(mobileDeviceDT.getId(), mobileDeviceDT);
        else
            throw new IllegalArgumentException("The mobile device " + mobileDeviceDT.getId() + " is already in memory.");
    }

    public void removeMobileDevice(UUID mobileDeviceId) {
        mobileDevices.remove(mobileDeviceId);
    }

    public Map<UUID, MobileDeviceDT> getMobileDevices() {
        return mobileDevices;
    }
}
