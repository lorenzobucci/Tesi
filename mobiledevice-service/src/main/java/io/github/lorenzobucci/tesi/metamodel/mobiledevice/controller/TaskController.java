package io.github.lorenzobucci.tesi.metamodel.mobiledevice.controller;

import io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao.TaskDao;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Task;
import jakarta.inject.Inject;

import java.util.List;

public class TaskController {

    @Inject
    private TaskDao taskDao;

    public Task getTask(long taskId) {
        return taskDao.findById(taskId);
    }

    public List<Task> retrieveTasks() {
        return taskDao.findAll();
    }
}
