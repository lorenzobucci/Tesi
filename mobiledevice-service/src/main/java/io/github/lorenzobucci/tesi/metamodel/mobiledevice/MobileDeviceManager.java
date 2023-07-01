package io.github.lorenzobucci.tesi.metamodel.mobiledevice;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MobileDeviceManager {
    private static MobileDeviceManager instance = null;

    private final Map<UUID, MobileDevice> mobileDevices = new HashMap<>();

    private MobileDeviceManager() {
    }

    public static MobileDeviceManager getInstance() {
        if (instance == null)
            instance = new MobileDeviceManager();
        return instance;
    }

    public void init(Set<MobileDevice> mobileDevices) {
        if (this.mobileDevices.isEmpty()) {
            for (MobileDevice mobileDevice : mobileDevices)
                addMobileDevice(mobileDevice);
        } else
            throw new IllegalStateException("Cannot initialize if there are mobile devices in memory.");
    }

    public void addMobileDevice(MobileDevice mobileDevice) {
        if (!mobileDevices.containsKey(mobileDevice.getId()))
            mobileDevices.putIfAbsent(mobileDevice.getId(), mobileDevice);
        else
            throw new IllegalArgumentException("The mobile device " + mobileDevice.getId() + " is already in memory.");
    }

    public void removeMobileDevice(UUID mobileDeviceId) {
        mobileDevices.remove(mobileDeviceId);
    }

    public Map<UUID, MobileDevice> getMobileDevices() {
        return mobileDevices;
    }
}
