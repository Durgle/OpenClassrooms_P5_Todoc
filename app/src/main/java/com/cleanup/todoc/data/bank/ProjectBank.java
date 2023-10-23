package com.cleanup.todoc.data.bank;

import com.cleanup.todoc.data.models.Project;

public class ProjectBank {

    private static ProjectBank mInstance;
    private final Project[] mProjects;

    public ProjectBank() {
        mProjects = new Project[]{
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2),
        };
    }

    public static ProjectBank getInstance() {
        if (mInstance == null) {
            mInstance = new ProjectBank();
        }
        return mInstance;
    }

    /**
     * Returns all the projects of the application.
     *
     * @return all the projects of the application
     */
    public Project[] getAllProject(){
        return mProjects;
    }

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    public Project getProjectById(long id){
        for (Project project : mProjects) {
            if (project.getId() == id)
                return project;
        }
        return null;
    }

}
