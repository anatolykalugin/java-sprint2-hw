package com.practicum.tasks;

import java.util.Objects;

public class Task {

    protected String name;
    protected String description;

    private final int id;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Название: " + name +
                ", Описание: " + description +
                ", id - " + id +
                ", Статус: " + status;
    }

    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public static Task createTask(int id, String status) {

//        Scanner taskScanner = new Scanner(System.in);
        System.out.println("Введите название задачи");
//        String name = taskScanner.next();
        String name = "Задача";
        System.out.println("Введите описание задачи");
//        String description = taskScanner.next();
        String description = "Описание задачи";

        Task task = new Task(name, description, id, status);

        System.out.println("Задача добавлена!");
        return task;
    }

    public int getId() {
        return id;
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
