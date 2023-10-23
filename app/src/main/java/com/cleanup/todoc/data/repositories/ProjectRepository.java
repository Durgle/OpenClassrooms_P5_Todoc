package com.cleanup.todoc.data.repositories;

import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.bank.ProjectBank;

public class ProjectRepository {

    /**
     * Get all existing {@link Project}
     *
     * @return List of project
     */
    public Project[] getAll() {
        return ProjectBank.getInstance().getAllProject();
    }

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id identifier
     * @return project
     */
    public Project getById(long id) {
        return ProjectBank.getInstance().getProjectById(id);
    }

}
