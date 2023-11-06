package com.cleanup.todoc.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final TaskDao mTaskDao;
    @NonNull
    private final MutableLiveData<List<Task>> mTasks = new MutableLiveData<>(new ArrayList<>());

    public TaskRepository(TaskDao taskDao) {
        mTaskDao = taskDao;
    }

    /**
     * Get all existing {@link Task}
     *
     * @return List of task
     */
    public LiveData<List<Task>> getAll() {
        return mTaskDao.getTasks();
    }

    /**
     * Get all existing {@link Task} with her {@link Project} associated
     *
     * @return List of task
     */
    public LiveData<List<TaskWithProject>> getAllTasksWithProjects() {
        return mTaskDao.getAllTasksWithProjects();
    }

    /**
     * Create new {@link Task}
     */
    public void create(@NonNull Task task) {
        mTaskDao.insertTask(task);
    }

    /**
     * Delete the given {@link Task}
     *
     * @param taskId Task unique identifier
     */
    public void delete(long taskId) {
        mTaskDao.deleteTaskById(taskId);
    }

}
