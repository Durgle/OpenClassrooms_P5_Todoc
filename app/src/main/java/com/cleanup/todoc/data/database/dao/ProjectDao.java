package com.cleanup.todoc.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.data.models.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM projects")
    LiveData<List<Project>> getProjects();

    @Insert
    void insertProject(Project project);

}
