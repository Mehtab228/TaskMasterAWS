package com.example.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmaster.model.Tasks;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insertTask(Tasks tasks);

    @Query("SELECT * FROM Tasks")
    public List<Tasks> findAll();

    @Query("SELECT * FROM Tasks WHERE id = :id")
    public Tasks findById(long id);

    @Query("SELECT * FROM Tasks WHERE state = :state")
    public List<Tasks> findAllByType(Tasks.State state);

    @Delete
    public void delete(Tasks tasks);

    @Update
    public void update(Tasks tasks);
}
