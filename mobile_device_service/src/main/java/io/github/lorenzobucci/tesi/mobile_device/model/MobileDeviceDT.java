package io.github.lorenzobucci.tesi.mobile_device.model;

import jakarta.persistence.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Table(name = "mobile_device")
public class MobileDeviceDT extends BaseEntity {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "past_trajectory_id")
    private Trajectory pastTrajectory = new Trajectory();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mobile_device_id", referencedColumnName = "id", nullable = false)
    private Set<Task> runningTasks = new HashSet<>();

    @Transient // CLASS INSTANCE PERSISTED USING PROPERTY MODE
    private TrajectoryForecaster trajectoryForecaster = new SampleTrajectoryForecaster(); // DEFAULT ALGORITHM

    @Column(name = "sync_left_to_optimize", nullable = false)
    private int syncLeftToOptimize = 10; // EVERY X SYNC AN OPTIMIZATION OF ALL TASKS IS PERFORMED

    public MobileDeviceDT() {

    }

    public void taskInvoked(String endpointURI, String parameters) {
        DependabilityRequirements requirements; // CALCULATE REQUIREMENTS BASED ON TRAJECTORY AND OTHER...
        requirements = new DependabilityRequirements(false, 0, 0); // SAMPLE REQUIREMENTS
        Task invokedTask = new Task(endpointURI, parameters, requirements);
        runningTasks.add(invokedTask);
    }

    public void taskCompleted(String endpointURI) throws NoSuchElementException {
        Task task = runningTasks.stream().filter(task1 -> task1.getEndpointURI().equals(endpointURI)).findAny().orElseThrow();
        if (runningTasks.contains(task)) {
            task.onCompleted();
            runningTasks.remove(task);
        } else
            throw new NoSuchElementException("There is no active task with URI " + endpointURI + " that belong to this mobile device.");
    }

    public void syncWithRealObject(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
        verifyOptimizationNeeded();
    }

    public void requestTaskOptimization(String endpointURI) {
        Task taskToOptimize = runningTasks.stream().filter(task -> task.getEndpointURI().equals(endpointURI)).findAny().orElseThrow();
        requestTaskOptimization(taskToOptimize);
    }

    private void requestTaskOptimization(Task taskToOptimize) {
        Trajectory forecastedTrajectory = trajectoryForecaster.forecast(getPastTrajectory());
        DependabilityRequirements newDependabilityRequirements;  // DO STUFF TO DETERMINE THE NEW REQUIREMENTS

        newDependabilityRequirements = new DependabilityRequirements(true, getCurrentPosition().getLatitude(), getCurrentPosition().getLongitude()); // SAMPLE REQUIREMENTS
        taskToOptimize.updateRequirements(newDependabilityRequirements);
    }

    private void verifyOptimizationNeeded() {
        if (syncLeftToOptimize > 0)
            syncLeftToOptimize--;
        else {
            syncLeftToOptimize = 10;
            for (Task task : runningTasks)
                requestTaskOptimization(task);
        }
    }

    public Trajectory getPastTrajectory() {
        return new Trajectory(pastTrajectory);
    }

    public Position getCurrentPosition() {
        return pastTrajectory.getLastPosition();
    }

    public Set<Task> getRunningTasks() {
        return runningTasks;
    }

    public TrajectoryForecaster getTrajectoryForecaster() {
        return trajectoryForecaster;
    }

    public void setTrajectoryForecaster(TrajectoryForecaster trajectoryForecaster) {
        this.trajectoryForecaster = trajectoryForecaster;
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "trajectory_forecaster_class")
    protected String getTrajectoryForecasterClass() {
        return trajectoryForecaster.getClass().getName();
    }

    protected void setTrajectoryForecasterClass(String trajectoryForecasterClass) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        trajectoryForecaster = (TrajectoryForecaster) Class.forName(trajectoryForecasterClass).getConstructor().newInstance();
    }
}
