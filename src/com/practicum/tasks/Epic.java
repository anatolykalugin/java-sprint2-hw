package com.practicum.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Epic extends Task {

    private LocalDateTime endDate;

    public Epic(String name, String description, int id, String status, TaskTypes type) {
        super(name, description, id, status, type);
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "Название: " + super.getName() +
                ", Описание: " + super.getDescription() +
                ", id - " + super.getId() +
                ", Статус: " + super.getStatus() +
                ", Тип: " + super.getType() +
                ", Длительность: " + getDuration().toMinutes() +
                ", Время начала: " + getStartTime().format(formatter) +
                ", Время конца: " + getEndDate().format(formatter);
    }
}