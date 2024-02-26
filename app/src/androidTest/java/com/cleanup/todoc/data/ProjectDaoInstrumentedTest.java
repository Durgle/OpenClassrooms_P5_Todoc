package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ProjectDaoInstrumentedTest extends DatabaseInstrumentedTest {

    private ProjectDao projectDao;

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();
        projectDao = database.projectDao();
    }

    @Test
    public void get_all_project() throws InterruptedException {

        Project project1 = new Project(1, "Projet Test 1", 0xFCEADAD1);
        Project project2 = new Project(2, "Projet Test 2", 0xFCEADAD2);
        Project project3 = new Project(3, "Projet Test 3", 0xFCEADAD3);

        projectDao.insertProject(project1);
        projectDao.insertProject(project2);
        projectDao.insertProject(project3);
        List<Project> results = LiveDataTestUtil.getOrAwaitValue(projectDao.getProjects());

        assertEquals(3, results.size());
        assertTrue(results.contains(project1));
        assertTrue(results.contains(project2));
        assertTrue(results.contains(project3));

    }

    @Test
    public void insert_project() throws InterruptedException {

        Project newProject = new Project(4, "Projet Test", 0xFCEADAD1);

        projectDao.insertProject(newProject);
        List<Project> results = LiveDataTestUtil.getOrAwaitValue(projectDao.getProjects());

        assertEquals(1, results.size());
        assertTrue(results.contains(newProject));

    }

}
