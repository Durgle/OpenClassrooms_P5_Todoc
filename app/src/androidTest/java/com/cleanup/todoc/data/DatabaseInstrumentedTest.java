package com.cleanup.todoc.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.data.database.TodocDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

abstract class DatabaseInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    protected TodocDatabase database;

    @Before
    public void setup() throws Exception {

        this.database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        TodocDatabase.class
                )
                .build();

    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

}
