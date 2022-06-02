package com.practicum.managers;

public class Managers {

    public HistoryManager searchHistory = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return new FileBackedTasksManager("resources/stuff.csv");
    }

    public HistoryManager getDefaultHistory() {
        return searchHistory;
    }
}
