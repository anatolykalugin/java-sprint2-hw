package com.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private int id;
    private String status;
    private TaskTypes type;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, int id, String status, TaskTypes type) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
        this.duration = Duration.ofMinutes(0);
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public TaskTypes getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public void addDuration(long duration) {
        this.duration = this.duration.plusMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "Название: " + name +
                ", Описание: " + description +
                ", id - " + id +
                ", Статус: " + status +
                ", Тип: " + type +
                ", Длительность: " + duration.toMinutes() +
                ", Время начала: " + startTime.format(formatter) +
                ", Время конца: " + getEndTime().format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && Objects.equals(description, task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
