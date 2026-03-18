package com.pao.laboratory03.bonus.model;

public class Task {
    private String id;
    private String title;
    private Status status;
    private Priority priority;
    private String assignee;

    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;
        this.assignee = null;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        String base = "Task{id='" + id + "', title='" + title + "', priority=" + priority + ", status=" + status;
        if (assignee != null) {
            base += ", assignee=" + assignee;
        }
        base += "}";
        return base;
    }
}