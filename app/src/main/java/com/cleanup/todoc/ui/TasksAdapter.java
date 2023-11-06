package com.cleanup.todoc.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;

/**
 * <p>Adapter which handles the list of tasks to display in the dedicated RecyclerView.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class TasksAdapter extends ListAdapter<TaskUiState, TasksAdapter.TaskViewHolder> {

    /**
     * The listener for when a task needs to be deleted
     */
    @NonNull
    private final DeleteTaskListener mDeleteTaskListener;

    protected TasksAdapter(@NonNull DeleteTaskListener deleteTaskListener) {
        super(DIFF_CALLBACK);
        mDeleteTaskListener = deleteTaskListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view,mDeleteTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    /**
     * Listener for deleting tasks
     */
    public interface DeleteTaskListener {
        /**
         * Called when a task needs to be deleted.
         *
         * @param task the task that needs to be deleted
         */
        void onDeleteTask(TaskUiState task);
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;

        /**
         * The TextView displaying the name of the task
         */
        private final TextView lblTaskName;

        /**
         * The TextView displaying the name of the project
         */
        private final TextView lblProjectName;

        /**
         * The delete icon
         */
        private final AppCompatImageView imgDelete;

        /**
         * Instantiates a new TaskViewHolder.
         *
         * @param itemView the view of the task item
         * @param deleteTaskListener the listener for when a task needs to be deleted to set
         */
        TaskViewHolder(@NonNull View itemView, @NonNull DeleteTaskListener deleteTaskListener) {
            super(itemView);

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(view -> {
                final Object tag = view.getTag();
                if (tag instanceof TaskUiState) {
                    deleteTaskListener.onDeleteTask((TaskUiState) tag);
                }
            });
        }

        /**
         * Binds a task to the item view.
         *
         * @param task the task to bind in the item view
         */
        void bind(TaskUiState task) {

            lblTaskName.setText(task.getTaskName());
            imgDelete.setTag(task);
            imgProject.setSupportImageTintList(ColorStateList.valueOf(task.getProjectColor()));
            lblProjectName.setText(task.getProjectName());

        }
    }

    public static final DiffUtil.ItemCallback<TaskUiState> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TaskUiState>() {
                @Override
                public boolean areItemsTheSame(@NonNull TaskUiState oldTask, @NonNull TaskUiState newTask) {
                    return oldTask.getTaskId() == newTask.getTaskId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull TaskUiState oldTask, @NonNull TaskUiState newTask) {
                    return oldTask.equals(newTask);
                }
            };
}
