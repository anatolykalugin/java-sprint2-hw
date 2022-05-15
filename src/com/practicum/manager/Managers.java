package com.practicum.manager;

public class Managers {

    public static HistoryManager searchHistory = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new FileBackedTasksManager("resources/stuff.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return searchHistory;
    }
}
