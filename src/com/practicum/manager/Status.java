package com.practicum.manager;

public enum Status {

    NEW("новое"), IN_PROGRESS("делаю"), DONE("выполнено");

    Status(String status) {
        this.status = status;
    }

    private final String status;

    public String getStatus() {
        return status;
    }
}
