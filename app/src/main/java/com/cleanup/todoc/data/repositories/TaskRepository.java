package com.cleanup.todoc.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    @NonNull
    private final MutableLiveData<List<Task>> mTasks = new MutableLiveData<>(new ArrayList<>());

    /**
     * Get all existing {@link Task}
     *
     * @return List of task
     */
    public LiveData<List<Task>> getAll() {
        return mTasks;
    }

    /**
     * Create new {@link Task}
     */
    public void create(@NonNull Task task) {
        List<Task> currentTasks = mTasks.getValue();
        if(currentTasks != null){
            List<Task> newList = new ArrayList<>(currentTasks);
            newList.add(task);
            mTasks.setValue(newList);
        }
    }

    /**
     * Delete the given {@link Task}
     *
     * @param task Task instance
     */
    public void delete(Task task) {
        List<Task> currentTasks = mTasks.getValue();
        if (currentTasks != null) {
            List<Task> newList = new ArrayList<>(currentTasks);
            newList.remove(task);

            mTasks.setValue(newList);
        }
    }

}
