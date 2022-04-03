package com.practicum.manager;

public final class Managers {

    private static final InMemoryHistoryManager searchHistory = new InMemoryHistoryManager();
    private static final TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(){
        return taskManager;
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return searchHistory;
    }
}
