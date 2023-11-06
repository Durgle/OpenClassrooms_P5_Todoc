package com.cleanup.todoc.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.cleanup.todoc.data.database.TodocDatabase;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectRepository mProjectRepository;
    private final TaskRepository mTaskRepository;
    private static ViewModelFactory factory;
    private static Executor mExecutor;

    public static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        mExecutor = Executors.newSingleThreadExecutor();
        mProjectRepository = new ProjectRepository(database.projectDao());
        mTaskRepository = new TaskRepository(database.taskDao());
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(this.mTaskRepository, this.mProjectRepository,mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        return ViewModelProvider.Factory.super.create(modelClass, extras);
    }
}