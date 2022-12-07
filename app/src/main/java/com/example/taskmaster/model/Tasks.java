package com.example.taskmaster.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Tasks {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    private String title;
    private String body;
    private State state;
    public enum State {
        NEW,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETE,
    }

    public Tasks(String title, String body, State state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String description) {
        this.body = description;
    }
}
