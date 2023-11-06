package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.ui.TaskUiState;
import com.cleanup.todoc.ui.TaskViewModel;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ProjectRepository mockProjectRepository;
    private TaskRepository mockTaskRepository;
    private Executor mockExecutor;
    private TaskViewModel mViewModel;
    private final List<TaskWithProject> fakeTasksWithProject = new ArrayList<>();
    private final List<Project> fakeProjects = new ArrayList<>();
    private static final long TIMESTAMP_28_OCT_2023 = 1698451200;
    private static final long TIMESTAMP_29_OCT_2023 = 1698537600;
    private static final long TIMESTAMP_1_NOV_2023 = 1698796800;
    private static final long TIMESTAMP_2_NOV_2023 = 1698883200;
    private static final long TIMESTAMP_3_NOV_2023 = 1698969600;

    @Before
    public void setup() {
        mockTaskRepository = Mockito.mock(TaskRepository.class);
        mockProjectRepository = Mockito.mock(ProjectRepository.class);
        mockExecutor = Mockito.mock(Executor.class);
        mockExecutorRun();
        mockGetAllTasksWithProjects();
        mViewModel = new TaskViewModel(mockTaskRepository, mockProjectRepository, mockExecutor);
    }

    private void mockExecutorRun() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(mockExecutor).execute(any());
    }

    private void mockGetAllTasksWithProjects() {

        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(1, 1, "Tache A", TIMESTAMP_28_OCT_2023),
                        new Project(1, "Projet 1", 0xFCEADAD1)
                ));
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(2, 3, "Tache B", TIMESTAMP_29_OCT_2023),
                        new Project(3, "Projet 3", 0xFCEADAD3)
                ));
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(5, 2, "Tache E", TIMESTAMP_3_NOV_2023),
                        new Project(2, "Projet 2", 0xFCEADAD2)
                ));
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(3, 3, "Tache C", TIMESTAMP_1_NOV_2023),
                        new Project(3, "Projet 3", 0xFCEADAD3)
                ));
        fakeTasksWithProject.add(
                new TaskWithProject(
                        new Task(4, 1, "Tache D", TIMESTAMP_2_NOV_2023),
                        new Project(1, "Projet 1", 0xFCEADAD1)
                ));
        LiveData<List<TaskWithProject>> fakeLiveDataTasksWithProject = new MutableLiveData<>(fakeTasksWithProject);
        Mockito.when(mockTaskRepository.getAllTasksWithProjects()).thenReturn(fakeLiveDataTasksWithProject);
    }

    private void mockProjectGetAll() {
        fakeProjects.add(new Project(1, "Projet 1", 0xFCEADAD1));
        fakeProjects.add(new Project(2, "Projet 2", 0xFCEADAD2));
        fakeProjects.add(new Project(3, "Projet 3", 0xFCEADAD3));
        LiveData<List<Project>> fakeLiveDataProjects = new MutableLiveData<>(fakeProjects);
        Mockito.when(mockProjectRepository.getAll()).thenReturn(fakeLiveDataProjects);
    }

    private List<TaskUiState> mapTaskWithProjectListToTaskUiList(List<TaskWithProject> tasksWithProjectList) {
        List<TaskUiState> list = new ArrayList<>();
        for (TaskWithProject taskWithProject : tasksWithProjectList) {
            Task task = taskWithProject.getTask();
            Project project = taskWithProject.getProject();
            TaskUiState taskUiState = new TaskUiState(task.getId(), task.getName(), project.getName(), project.getColor());

            list.add(taskUiState);
        }
        return list;
    }

    @Test
    public void test_method_add_task() {
        final long projectId = 1;
        final String name = "Nouvelle tache";
        final long creationTimestamp = new Date().getTime();
        final Task expectedParam = new Task(projectId, name, creationTimestamp);

        ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);

        mViewModel.addTask(projectId, name, creationTimestamp);
        verify(mockTaskRepository).create(argumentCaptor.capture());
        Task actualArgument = argumentCaptor.getValue();

        assertEquals(actualArgument.getName(), expectedParam.getName());
        assertEquals(actualArgument.getProjectId(), expectedParam.getProjectId());
        assertEquals(actualArgument.getCreationTimestamp(), expectedParam.getCreationTimestamp());
    }

    @Test
    public void test_method_delete_task() {
        final TaskUiState TaskUiStateToDelete = new TaskUiState(
                1L, "Tache a supprimer", "Projet 2", 0xFCEADAD1
        );

        mViewModel.deleteTask(TaskUiStateToDelete);
        verify(mockTaskRepository).delete(eq(1L));
    }

    @Test
    public void test_method_get_tasks() throws InterruptedException {
        List<TaskUiState> expectedList = mapTaskWithProjectListToTaskUiList(fakeTasksWithProject);
        List<TaskUiState> tasks = LiveDataTestUtil.getOrAwaitValue(mViewModel.getTasks());

        assertEquals(expectedList, tasks);
    }

    @Test
    public void test_method_get_tasks_with_sort() throws InterruptedException {
        Collections.sort(fakeTasksWithProject, new TaskWithProject.TaskAZComparator());
        List<TaskUiState> expectedList = mapTaskWithProjectListToTaskUiList(fakeTasksWithProject);
        mViewModel.onSortChanged(R.id.filter_alphabetical);
        List<TaskUiState> sortedTasksAZ = LiveDataTestUtil.getOrAwaitValue(mViewModel.getTasks());

        assertEquals(expectedList, sortedTasksAZ);
    }

    @Test
    public void test_method_get_projects() throws InterruptedException {
        mockProjectGetAll();
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(mViewModel.getProjects());
        assertEquals(projects, fakeProjects);
    }

}
