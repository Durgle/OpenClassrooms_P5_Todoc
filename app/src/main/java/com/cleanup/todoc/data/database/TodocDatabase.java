package com.cleanup.todoc.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.models.Project;
import com.cleanup.todoc.data.models.Task;

import java.util.concurrent.Executors;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile TodocDatabase INSTANCE;

    // --- DAO ---
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    // --- INSTANCE ---
    public static TodocDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TodocDatabase.class, "TodocDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;

    }

    private static Callback prepopulateDatabase() {

        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(() -> {
                    INSTANCE.projectDao().insertProject(
                            new Project(1L, "Projet Tartampion", 0xFFEADAD1)
                    );
                    INSTANCE.projectDao().insertProject(
                            new Project(2L, "Projet Lucidia", 0xFFB4CDBA)
                    );
                    INSTANCE.projectDao().insertProject(
                            new Project(3L, "Projet Circus", 0xFFA3CED2)
                    );
                });
            }

        };

    }

}
