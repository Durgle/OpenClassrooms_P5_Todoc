package com.cleanup.todoc.ui;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.Objects;

public class TaskUiState {

    /**
     * The unique identifier of the task
     */
    private final long taskId;

    /**
     * The name of the task
     */
    @NonNull
    private final String taskName;

    /**
     * The name of the project
     */
    @NonNull
    private final String projectName;

    /**
     * The hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    private final int projectColor;

    /**
     * TaskUiState constructor
     *
     * @param taskId       The unique identifier of the task
     * @param taskName     The name of the task
     * @param projectName  The name of the project
     * @param projectColor The hex (ARGB) code of the color associated to the project
     */
    public TaskUiState(long taskId, @NonNull String taskName, @NonNull String projectName, int projectColor) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.projectName = projectName;
        this.projectColor = projectColor;
    }

    /**
     * Returns the unique identifier of the task.
     *
     * @return the unique identifier of the task
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * Returns the name of the task.
     *
     * @return the name of the task
     */
    @NonNull
    public String getTaskName() {
        return taskName;
    }

    /**
     * Returns the name of the project associated to the task.
     *
     * @return the name of the project associated to the task
     */
    @NonNull
    public String getProjectName() {
        return projectName;
    }

    /**
     * Returns the hex (ARGB) code of the color associated to the project.
     *
     * @return the hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    public int getProjectColor() {
        return projectColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskUiState that = (TaskUiState) o;
        return taskId == that.taskId && projectColor == that.projectColor && taskName.equals(that.taskName) && projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskName, projectName, projectColor);
    }

}
