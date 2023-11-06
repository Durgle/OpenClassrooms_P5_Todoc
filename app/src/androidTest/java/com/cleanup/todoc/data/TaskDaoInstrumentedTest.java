package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;
import com.cleanup.todoc.data.models.TaskWithProject;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TaskDaoInstrumentedTest extends DatabaseInstrumentedTest {

    private TaskDao taskDao;

    private static final long PROJECT_ID_1 = 1;
    private static final long PROJECT_ID_2 = 2;
    private static final long PROJECT_ID_3 = 3;
    private static final Project PROJECT_1 = new Project(PROJECT_ID_1, "Projet Tartampion", 0xFFEADAD1);
    private static final Project PROJECT_2 = new Project(PROJECT_ID_2, "Projet Lucidia", 0xFFB4CDBA);
    private static final Project PROJECT_3 = new Project(PROJECT_ID_3, "Projet Circus", 0xFFA3CED2);

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();
        taskDao = database.taskDao();
        database.projectDao().insertProject(PROJECT_1);
        database.projectDao().insertProject(PROJECT_2);
        database.projectDao().insertProject(PROJECT_3);
    }

    @Test
    public void get_all_task() throws InterruptedException {
        long creationTimeStamp = new Date().getTime();
        Task task1 = new Task(1, PROJECT_ID_1, "Tache 1", creationTimeStamp);
        Task task2 = new Task(2, PROJECT_ID_2, "Tache 2", creationTimeStamp);
        Task task3 = new Task(3, PROJECT_ID_3, "Tache 3", creationTimeStamp);
        long taskId1 = taskDao.insertTask(task1);
        long taskId2 = taskDao.insertTask(task2);
        long taskId3 = taskDao.insertTask(task3);
        List<Task> results = LiveDataTestUtil.getOrAwaitValue(taskDao.getTasks());

        assertEquals(3, results.size());
        assertEquals(1, taskId1);
        assertEquals(2, taskId2);
        assertEquals(3, taskId3);
        assertTrue(results.contains(task1));
        assertTrue(results.contains(task2));
        assertTrue(results.contains(task3));

    }

    @Test
    public void insert_task() throws InterruptedException {
        String taskName = "Tache 1";
        long creationTimeStamp = new Date().getTime();
        Task task = new Task(PROJECT_ID_1, taskName, creationTimeStamp);
        long taskId = taskDao.insertTask(task);
        List<Task> results = LiveDataTestUtil.getOrAwaitValue(taskDao.getTasks());
        Task taskSaved = results.get(0);

        assertEquals(1, taskId);
        assertEquals(1, results.size());
        assertEquals(taskId, taskSaved.getProjectId());
        assertEquals(taskName, taskSaved.getName());
        assertEquals(PROJECT_ID_1, taskSaved.getProjectId());
        assertEquals(creationTimeStamp, taskSaved.getCreationTimestamp());

    }

    @Test
    public void get_all_tasks_with_projects() throws InterruptedException {
        long creationTimeStamp = new Date().getTime();
        Task task1 = new Task(1, PROJECT_ID_1, "Tache 1", creationTimeStamp);
        Task task2 = new Task(2, PROJECT_ID_2, "Tache 2", creationTimeStamp);
        long taskId1 = taskDao.insertTask(task1);
        long taskId2 = taskDao.insertTask(task2);
        List<TaskWithProject> results = LiveDataTestUtil.getOrAwaitValue(taskDao.getAllTasksWithProjects());

        assertEquals(2, results.size());
        assertTrue(results.contains(new TaskWithProject(task1, PROJECT_1)));
        assertTrue(results.contains(new TaskWithProject(task2, PROJECT_2)));
    }

    @Test
    public void delete_task_by_id() throws InterruptedException {
        Task task = new Task(1, PROJECT_ID_1, "Tache 1", new Date().getTime());
        Task task2 = new Task(2, PROJECT_ID_1, "Tache 2", new Date().getTime());
        long taskId = taskDao.insertTask(task);
        taskDao.insertTask(task2);
        taskDao.deleteTaskById(taskId);
        List<Task> resultAfterDelete = LiveDataTestUtil.getOrAwaitValue(taskDao.getTasks());

        assertEquals(1, resultAfterDelete.size());
        assertFalse(resultAfterDelete.contains(task));
        assertTrue(resultAfterDelete.contains(task2));
    }
}
