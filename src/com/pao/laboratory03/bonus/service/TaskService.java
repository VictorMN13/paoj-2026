package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.model.*;
import com.pao.laboratory03.bonus.exception.*;
import java.util.*;

public class TaskService {
    private static TaskService instance;
    private int taskCnt = 0;

    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;

    private TaskService() {
        this.tasksById = new HashMap<>();
        this.tasksByPriority = new EnumMap<>(Priority.class);
        this.auditLog = new ArrayList<>();
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    // a)
    public Task addTask(String title, Priority priority) {
        taskCnt++;
        String id = String.format("T%03d", taskCnt);
        Task newTask = new Task(id, title, priority);
        tasksById.put(id, newTask);
        tasksByPriority.get(priority).add(newTask);
        auditLog.add(String.format("[ADD] %s: '%s' (%s)", id, title, priority));
        return newTask;
    }

    // b)
    public void assignTask(String taskId, String assignee) {
        Task task = tasksById.get(taskId);
        if (task == null) throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");

        task.setAssignee(assignee);
        auditLog.add(String.format("[ASSIGN] %s → %s", taskId, assignee));
    }

    // c)
    public void changeStatus(String taskId, Status newStatus) {
        Task task = tasksById.get(taskId);
        if (task == null) throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        Status oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add(String.format("[STATUS] %s: %s → %s", taskId, oldStatus, newStatus));
    }

    // d)
    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.getOrDefault(priority, new ArrayList<>()));
    }

    // e)
    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Task task : tasksById.values()) {
            Status status = task.getStatus();
            long cnt = summary.getOrDefault(status, 0L);
            summary.put(status, cnt + 1);
        }
        return summary;
    }

    // f)
    public List<Task> getUnassignedTasks() {
        ArrayList<Task> unassignedTasks = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) unassignedTasks.add(task);
        }
        return unassignedTasks;
    }

    // g)
    public void printAuditLog() {
        System.out.println("=== Jurnal Audit ===");
        for (String s : auditLog) {
            System.out.println(s);
        }
    }

    // h)
    public double getTotalUrgencyScore(int baseDays) {
        double cnt = 0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() == null || task.getStatus() == Status.DONE || task.getStatus() == Status.CANCELLED)
                continue;
            cnt += task.getPriority().calculateScore(baseDays);
        }
        return cnt;
    }
}
