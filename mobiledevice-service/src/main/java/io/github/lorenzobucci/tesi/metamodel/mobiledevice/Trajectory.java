package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import java.util.Comparator;
import java.util.TreeSet;

public class Trajectory {

    private final TreeSet<Position> positionsSet;

    public Trajectory(Trajectory trajectory) {
        positionsSet = trajectory.getPositionsSet();
    }

    public Trajectory() {
        positionsSet = new TreeSet<>(Comparator.comparing(Position::timestamp));
    }

    public TreeSet<Position> getPositionsSet() {
        return new TreeSet<>(positionsSet);
    }

    public void addPosition(Position position) {
        positionsSet.add(position);
    }
}
