package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.utils.SortMethodEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskViewModel extends ViewModel {

    private final TaskRepository mTaskRepository;
    private final ProjectRepository mProjectRepository;
    private final MediatorLiveData<List<Task>> mTasks = new MediatorLiveData<>();
    private final MutableLiveData<SortMethodEnum> mSort = new MutableLiveData<>();

    public TaskViewModel(TaskRepository taskRepository, ProjectRepository projectRepository) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;

        mTasks.addSource(
                mTaskRepository.getAll(), taskList -> combine(taskList, mSort.getValue())
        );
        mTasks.addSource(
                mSort, sort -> combine(mTaskRepository.getAll().getValue(), sort)
        );
    }

    private void combine(@Nullable List<Task> tasks, @Nullable SortMethodEnum sortMethod) {

        if(sortMethod != null && tasks != null) {
            List<Task> newTaskList = new ArrayList<>(tasks);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(newTaskList, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(newTaskList, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(newTaskList, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(newTaskList, new Task.TaskOldComparator());
                    break;

            }
            mTasks.setValue(newTaskList);
        } else {
            mTasks.setValue(tasks);
        }


    }

    /**
     * Add a new task
     */
    public void addTask(long id, long projectId, @NonNull String name, long creationTimestamp){
        mTaskRepository.create(
                new Task(
                        id,
                        projectId,
                        name,
                        creationTimestamp
                )
        );
    }

    /**
     * Delete the given task
     */
    public void deleteTask(Task task){
        mTaskRepository.delete(task);
    }

    /**
     * Return all tasks
     *
     * @return A liveData containing a task list
     */
    public LiveData<List<Task>> getTasks() {
        return mTasks;
    }

    /**
     * Return all projects
     *
     * @return Project list
     */
    public Project[] getProjects() {
        return mProjectRepository.getAll();
    }

    public void onSortChanged(int id) {
        if (id == R.id.filter_alphabetical) {
            mSort.setValue(SortMethodEnum.ALPHABETICAL);
        } else if (id == R.id.filter_alphabetical_inverted) {
            mSort.setValue(SortMethodEnum.ALPHABETICAL_INVERTED);
        } else if (id == R.id.filter_oldest_first) {
            mSort.setValue(SortMethodEnum.OLD_FIRST);
        } else if (id == R.id.filter_recent_first) {
            mSort.setValue(SortMethodEnum.RECENT_FIRST);
        } else {
            mSort.setValue(SortMethodEnum.NONE);
        }
    }
}
