package com.cleanup.todoc.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(JUnit4.class)
public class TaskRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private TaskRepository mTaskRepository;
    private TaskDao mockTaskDao;
    private final List<Task> fakeTasks = new ArrayList<>();
    private final List<TaskWithProject> fakeTasksWithProject = new ArrayList<>();
    private static final long TASK_TO_DELETE_ID = 2;

    @Before
    public void setup() {
        mockTaskDao = Mockito.mock(TaskDao.class);
        mTaskRepository = new TaskRepository(mockTaskDao);
    }

    private void mockDaoGetTasks() {
        fakeTasks.add(new Task(1, 1, "Tache 1", new Date().getTime()));
        fakeTasks.add(new Task(2, 3, "Tache 2", new Date().getTime()));
        LiveData<List<Task>> fakeLiveDataTasks = new MutableLiveData<>(fakeTasks);
        Mockito.when(mockTaskDao.getTasks()).thenReturn(fakeLiveDataTasks);
    }

    private void mockDaoGetAllTasksWithProjects() {
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(1, 1, "Tache 1", new Date().getTime()),
                        new Project(1, "Projet 1", 0xFCEADAD1)
                ));
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(2, 3, "Tache 2", new Date().getTime()),
                        new Project(3, "Projet 3", 0xFCEADAD1)
                ));
        LiveData<List<TaskWithProject>> fakeLiveDataTasksWithProject = new MutableLiveData<>(fakeTasksWithProject);
        Mockito.when(mockTaskDao.getAllTasksWithProjects()).thenReturn(fakeLiveDataTasksWithProject);
    }

    @Test
    public void test_method_get_all() throws InterruptedException {
        mockDaoGetTasks();
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(mTaskRepository.getAll());
        assertEquals(2, tasks.size());
        assertEquals(fakeTasks, tasks);

    }

    @Test
    public void test_method_get_all_tasks_with_projects() throws InterruptedException {
        mockDaoGetAllTasksWithProjects();
        List<TaskWithProject> tasksWithProject = LiveDataTestUtil.getOrAwaitValue(mTaskRepository.getAllTasksWithProjects());
        assertEquals(2, tasksWithProject.size());
        assertEquals(fakeTasksWithProject, tasksWithProject);
    }

    @Test
    public void test_method_insert_task() throws InterruptedException {
        Task newTask = new Task(1, 1, "Nouvelle Tache", 0xFCEADAD1);
        mTaskRepository.create(newTask);
        verify(mockTaskDao).insertTask(ArgumentMatchers.eq(newTask));
        verifyNoMoreInteractions(mockTaskDao);
    }

    @Test
    public void test_method_delete_by_id() {
        mTaskRepository.delete(TASK_TO_DELETE_ID);
        verify(mockTaskDao).deleteTaskById(ArgumentMatchers.eq(TASK_TO_DELETE_ID));
        verifyNoMoreInteractions(mockTaskDao);
    }

}
