package com.example.taskmaster.model;

public class Tasks {
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
