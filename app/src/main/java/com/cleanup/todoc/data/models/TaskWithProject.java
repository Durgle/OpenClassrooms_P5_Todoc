package com.cleanup.todoc.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Comparator;
import java.util.Objects;

public class TaskWithProject {

    @Embedded
    private final Task task;

    @Relation(parentColumn = "project_id", entity = Project.class, entityColumn = "id")
    private final Project project;

    public TaskWithProject(Task task, Project project) {
        this.task = task;
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public Project getProject() {
        return project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskWithProject that = (TaskWithProject) o;
        return Objects.equals(task, that.task) && Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, project);
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return new Task.TaskAZComparator().compare(left.task, right.task);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return new Task.TaskZAComparator().compare(left.task, right.task);
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return new Task.TaskRecentComparator().compare(left.task, right.task);
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return new Task.TaskOldComparator().compare(left.task, right.task);
        }
    }
}
