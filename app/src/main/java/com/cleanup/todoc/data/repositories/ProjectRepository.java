package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.models.Project;

import java.util.List;

public class ProjectRepository {

    private final ProjectDao mProjectDao;

    public ProjectRepository(ProjectDao projectDao) {
        mProjectDao = projectDao;
    }

    /**
     * Get all existing {@link Project}
     *
     * @return List of project
     */
    public LiveData<List<Project>> getAll() {
        return mProjectDao.getProjects();
    }

}
