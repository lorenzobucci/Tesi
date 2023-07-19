package io.github.lorenzobucci.tesi.metamodel.mobiledevice.controller;

import io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao.TaskDao;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Task;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;

public class TaskController {

    @Inject
    private TaskDao taskDao;

    public Task getTask(long taskId) throws NoSuchElementException {
        Task task = taskDao.findById(taskId);
        if (task != null)
            return task;
        else
            throw new NoSuchElementException("Task with id=" + taskId + " does not exist.");
    }

    public List<Task> retrieveTasks() {
        return taskDao.findAll();
    }
}
