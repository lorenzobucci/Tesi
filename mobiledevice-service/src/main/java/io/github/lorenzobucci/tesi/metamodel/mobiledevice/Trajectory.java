package io.github.lorenzobucci.tesi.metamodel.mobiledevice;

import java.util.Comparator;
import java.util.TreeSet;

public class Trajectory {

    private final TreeSet<Position> positionsSet;

    public Trajectory() {
        positionsSet = new TreeSet<>(Comparator.comparing(Position::timestamp));
    }

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
