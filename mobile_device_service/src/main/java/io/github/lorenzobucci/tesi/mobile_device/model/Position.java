package io.github.lorenzobucci.tesi.mobile_device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "position")
public class Position extends BaseEntity implements Comparable<Position> {

    private float latitude;
    private float longitude;

    @Column(nullable = false)
    private Timestamp timestamp;

    public Position(float latitude, float longitude, Timestamp timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    protected Position() {
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Position o) {
        return this.timestamp.compareTo(o.getTimestamp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (Float.compare(position.latitude, latitude) != 0) return false;
        if (Float.compare(position.longitude, longitude) != 0) return false;
        return timestamp.equals(position.timestamp);
    }

    @Override
    public int hashCode() {
        int result = (latitude != 0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != 0.0f ? Float.floatToIntBits(longitude) : 0);
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
