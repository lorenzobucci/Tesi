package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SortNatural;

import java.util.TreeSet;

@Entity
@Table(name = "trajectory")
public class Trajectory extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @SortNatural
    @JoinColumn(name = "trajectory_id", referencedColumnName = "id")
    private TreeSet<Position> positionsSet = new TreeSet<>();

    @SuppressWarnings("CopyConstructorMissesField")
    Trajectory(Trajectory trajectory) {
        positionsSet = new TreeSet<>(trajectory.positionsSet);
    }

    protected Trajectory() {
    }

    public void addPosition(Position position) {
        positionsSet.add(position);
    }

    public TreeSet<Position> getPositionsSet() {
        return positionsSet;
    }

    Position getLastPosition() {
        return positionsSet.last();
    }
}
