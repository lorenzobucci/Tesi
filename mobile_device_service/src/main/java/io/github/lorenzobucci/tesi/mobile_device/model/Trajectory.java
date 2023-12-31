package io.github.lorenzobucci.tesi.mobile_device.model;

import jakarta.persistence.*;

import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "trajectory")
public class Trajectory extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "trajectory_id", referencedColumnName = "id")
    private SortedSet<Position> positionsSet = new TreeSet<>();

    Trajectory(Trajectory trajectory) {
        positionsSet = new TreeSet<>(trajectory.positionsSet);
    }

    protected Trajectory() {
    }

    public void addPosition(Position position) {
        positionsSet.add(position);
    }

    public SortedSet<Position> getPositionsSet() {
        return positionsSet;
    }

    Position getLastPosition() {
        return positionsSet.last();
    }
}
