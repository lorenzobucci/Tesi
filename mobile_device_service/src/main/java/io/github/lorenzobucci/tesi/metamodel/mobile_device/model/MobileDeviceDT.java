package io.github.lorenzobucci.tesi.metamodel.mobile_device.model;

import jakarta.persistence.*;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Table(name = "mobile_device")
public class MobileDeviceDT extends BaseEntity {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "past_trajectory_id")
    private Trajectory pastTrajectory = new Trajectory();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mobile_device_id", referencedColumnName = "id")
    private Set<Task> runningTasks = new HashSet<>();

    @Transient // CLASS INSTANCE PERSISTED USING PROPERTY MODE
    private TrajectoryForecaster trajectoryForecaster = new SampleTrajectoryForecaster(); // DEFAULT ALGORITHM

    public MobileDeviceDT() {

    }

    public void taskInvoked(URI endpoint, String parameters) {
        DependabilityRequirements requirements = new DependabilityRequirements(); // CALCULATE REQUIREMENTS BASED ON TRAJECTORY AND OTHER...
        Task invokedTask = new Task(endpoint, parameters, requirements);
        runningTasks.add(invokedTask);
    }

    public void requestTaskOptimization(URI endpoint) { // BUSINESS LOGIC OR EXTERNAL CALL?
        Task taskToOptimize = runningTasks.stream().filter(task -> task.getEndpoint().equals(endpoint)).findAny().orElseThrow();

        Trajectory forecastedTrajectory = trajectoryForecaster.forecast(getPastTrajectory());
        DependabilityRequirements newDependabilityRequirements = new DependabilityRequirements();  // DO STUFF TO DETERMINE THE NEW REQUIREMENTS
        taskToOptimize.updateRequirements(newDependabilityRequirements);
    }

    public void taskCompleted(Task task) throws NoSuchElementException {
        if (runningTasks.contains(task)) {
            task.onCompleted();
            runningTasks.remove(task);
        } else
            throw new NoSuchElementException("Task " + task.getId() + " does not belong to this mobile device.");
    }

    public void syncWithRealObject(Position currentPosition) {
        pastTrajectory.addPosition(currentPosition);
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
