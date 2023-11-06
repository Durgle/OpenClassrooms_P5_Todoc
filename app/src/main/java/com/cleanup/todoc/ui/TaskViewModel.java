package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.utils.SortMethodEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final TaskRepository mTaskRepository;
    private final ProjectRepository mProjectRepository;
    private final Executor mExecutor;
    private final LiveData<List<TaskWithProject>> mTasks;
    private final MediatorLiveData<List<TaskUiState>> mSortedTask = new MediatorLiveData<>();
    private final MutableLiveData<SortMethodEnum> mSort = new MutableLiveData<>();

    public TaskViewModel(TaskRepository taskRepository, ProjectRepository projectRepository, Executor executor) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;
        mExecutor = executor;
        mTasks = mTaskRepository.getAllTasksWithProjects();

        mSortedTask.addSource(
                mTasks, taskList -> combine(taskList, mSort.getValue())
        );
        mSortedTask.addSource(
                mSort, sort -> combine(mTasks.getValue(), sort)
        );
    }

    private void combine(@Nullable List<TaskWithProject> taskWithProjects, @Nullable SortMethodEnum sortMethod) {

        if (taskWithProjects != null) {

            if (sortMethod != null) {
                switch (sortMethod) {
                    case ALPHABETICAL:
                        Collections.sort(taskWithProjects, new TaskWithProject.TaskAZComparator());
                        break;
                    case ALPHABETICAL_INVERTED:
                        Collections.sort(taskWithProjects, new TaskWithProject.TaskZAComparator());
                        break;
                    case RECENT_FIRST:
                        Collections.sort(taskWithProjects, new TaskWithProject.TaskRecentComparator());
                        break;
                    case OLD_FIRST:
                        Collections.sort(taskWithProjects, new TaskWithProject.TaskOldComparator());
                        break;
                }
            }

            List<TaskUiState> newTaskList = new ArrayList<>();
            for (TaskWithProject taskWithProject : taskWithProjects) {
                Task task = taskWithProject.getTask();
                Project project = taskWithProject.getProject();
                TaskUiState taskUiState = new TaskUiState(task.getId(), task.getName(), project.getName(), project.getColor());

                newTaskList.add(taskUiState);
            }

            mSortedTask.setValue(newTaskList);

        } else {
            mSortedTask.setValue(null);
        }

    }

    /**
     * Add a new task
     */
    public void addTask(long projectId, @NonNull String name, long creationTimestamp) {
        mExecutor.execute(() -> mTaskRepository.create(
                new Task(
                        projectId,
                        name,
                        creationTimestamp
                )
        ));
    }

    /**
     * Delete the given task
     */
    public void deleteTask(TaskUiState task) {
        mExecutor.execute(() -> mTaskRepository.delete(task.getTaskId()));
    }

    /**
     * Return all tasks
     *
     * @return A liveData containing a task list
     */
    public LiveData<List<TaskUiState>> getTasks() {
        return mSortedTask;
    }

    /**
     * Return all projects
     *
     * @return Project list
     */
    public LiveData<List<Project>> getProjects() {
        return mProjectRepository.getAll();
    }

    /**
     * Define sort filter
     *
     * @param id sort identifier
     */
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
