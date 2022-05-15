package com.practicum.tasks;

public class Subtask extends Task {

    private final int epicLinkId;

    public Subtask(String name, String description, int id, String status, TaskTypes type, int epicLinkId) {
        super(name, description, id, status, type);
        this.epicLinkId = epicLinkId;
    }

    public int getEpicLinkId() {
        return epicLinkId;
    }

    @Override
    public String toString() {
        return super.toString() + ", Связка: " + epicLinkId;
    }

}
