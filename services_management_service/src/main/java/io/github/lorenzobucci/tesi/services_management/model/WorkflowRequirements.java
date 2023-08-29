package io.github.lorenzobucci.tesi.services_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class WorkflowRequirements {

    // SAMPLE REQUIREMENTS
    @Column(name = "proximity_computation")
    private boolean proximityComputation;

    @Column(name = "preferred_latitude")
    private float preferredLatitude;

    @Column(name = "preferred_longitude")
    private float preferredLongitude;

    public WorkflowRequirements(boolean proximityComputation, float preferredLatitude, float preferredLongitude) {
        this.proximityComputation = proximityComputation;
        this.preferredLatitude = preferredLatitude;
        this.preferredLongitude = preferredLongitude;
    }

    protected WorkflowRequirements() {

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
