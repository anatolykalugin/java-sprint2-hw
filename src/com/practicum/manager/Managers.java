package com.practicum.manager;

public class Managers {

    public static InMemoryHistoryManager searchHistory = new InMemoryHistoryManager();
    public static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static InMemoryTaskManager getDefault(){
        return taskManager;
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return searchHistory;
    }
}
