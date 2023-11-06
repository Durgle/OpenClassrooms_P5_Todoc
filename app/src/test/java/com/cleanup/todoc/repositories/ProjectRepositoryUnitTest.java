package com.cleanup.todoc.repositories;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class ProjectRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ProjectRepository mProjectRepository;
    private ProjectDao mockProjectDao;
    private final List<Project> fakeProjects = new ArrayList<>();
    private Project fakeProject;
    private static final long EXPECTED_PROJECT_ID = 1;

    @Before
    public void setup() {
        mockProjectDao = Mockito.mock(ProjectDao.class);
        mProjectRepository = new ProjectRepository(mockProjectDao);
    }

    private void mockDaoGetProjects() {
        fakeProjects.add(new Project(1, "Projet 1", 0xFCEADAD1));
        fakeProjects.add(new Project(2, "Projet 2", 0xFCEADAD1));
        LiveData<List<Project>> fakeLiveDataProjects = new MutableLiveData<>(fakeProjects);
        Mockito.when(mockProjectDao.getProjects()).thenReturn(fakeLiveDataProjects);
    }

    @Test
    public void test_method_get_all() throws InterruptedException {
        mockDaoGetProjects();
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(mProjectRepository.getAll());
        assertEquals(2, projects.size());
        assertEquals(fakeProjects, projects);

    }

}
