package io.github.lorenzobucci.tesi.metamodel.mobiledevice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SortNatural;

import java.util.TreeSet;
import java.util.UUID;

@Entity(name = "trajectory")
public class Trajectory {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @SortNatural
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "position_id"), name = "trajectory_positions")
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

    TreeSet<Position> getPositionsSet() {
        return positionsSet;
    }

    Position getLastPosition() {
        return positionsSet.last();
    }
}
