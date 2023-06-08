package mobiledevice;

import java.sql.Timestamp;

public record Position(float latitude, float longitude, Timestamp timestamp) {
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
        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
