package com.cleanup.todoc.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getTasks();

    @Insert
    long insertTask(Task task);

    @Transaction
    @Query("SELECT * FROM tasks")
    LiveData<List<TaskWithProject>> getAllTasksWithProjects();

    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteTaskById(long id);
}
