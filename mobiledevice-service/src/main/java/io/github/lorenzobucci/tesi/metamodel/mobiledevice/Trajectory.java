package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import jakarta.persistence.*;
import org.hibernate.annotations.SortNatural;

import java.util.TreeSet;
import java.util.UUID;

@Entity(name = "trajectory")
public class Trajectory {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(cascade = {CascadeType.ALL})
    @SortNatural
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "position_id"), name = "trajectory_positions")
    private TreeSet<Position> positionsSet = new TreeSet<>();

    public Trajectory() {
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Trajectory(Trajectory trajectory) {
        positionsSet = new TreeSet<>(trajectory.positionsSet);
    }

    TreeSet<Position> getPositionsSet() {
        return positionsSet;
    }

    public void addPosition(Position position) {
        positionsSet.add(position);
    }
}
