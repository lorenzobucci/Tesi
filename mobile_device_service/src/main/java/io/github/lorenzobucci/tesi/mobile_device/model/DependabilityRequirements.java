package io.github.lorenzobucci.tesi.mobile_device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DependabilityRequirements {

    // SAMPLE REQUIREMENTS

    @Column(name = "proximity_computation")
    private boolean proximityComputation;

    @Column(name = "preferred_latitude")
    private float preferredLatitude;

    @Column(name = "preferred_longitude")
    private float preferredLongitude;

    DependabilityRequirements(boolean proximityComputation, float preferredLatitude, float preferredLongitude) {
        this.proximityComputation = proximityComputation;
        this.preferredLatitude = preferredLatitude;
        this.preferredLongitude = preferredLongitude;
    }

    protected DependabilityRequirements() {

    }

    public boolean isProximityComputation() {
        return proximityComputation;
    }

    public float getPreferredLatitude() {
        return preferredLatitude;
    }

    public float getPreferredLongitude() {
        return preferredLongitude;
    }
}
