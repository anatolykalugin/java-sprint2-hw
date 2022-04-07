package com.practicum.manager;

public class Managers {

    public static HistoryManager searchHistory = new InMemoryHistoryManager();
    public static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return searchHistory;
    }
}
