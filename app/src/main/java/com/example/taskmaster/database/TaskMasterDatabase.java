package com.example.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskmaster.dao.TaskDao;
import com.example.taskmaster.model.Tasks;

@TypeConverters({TaskMasterDatabaseConverter.class})
@Database(entities = {Tasks.class}, version = 1)
public abstract class TaskMasterDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

}
