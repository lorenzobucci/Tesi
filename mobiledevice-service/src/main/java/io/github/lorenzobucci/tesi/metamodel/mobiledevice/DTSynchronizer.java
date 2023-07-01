package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import java.util.NoSuchElementException;
import java.util.UUID;

public class DTSynchronizer {

    private static final MobileDeviceManager manager = MobileDeviceManager.getInstance();

    private DTSynchronizer() {
    }

    public static void syncMobileDeviceProperties(UUID mobileDeviceId, Position currentPosition) {
        if (manager.getMobileDevices().containsKey(mobileDeviceId))
            manager.getMobileDevices().get(mobileDeviceId).syncWithRealObject(currentPosition);
        else
            throw new NoSuchElementException("The mobile device " + mobileDeviceId + " does not exist.");
    }

}
