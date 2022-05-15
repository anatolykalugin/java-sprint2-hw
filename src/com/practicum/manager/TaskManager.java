package com.practicum.manager;

import com.practicum.tasks.Task;

public interface TaskManager {

    void updateTask(int id, String status);

    void searchAndDelete(int i);

    void showSubs(int i);

    void printMenu();

    Task searchForTask(int i);

    void showEverything();

    void clearCategory(int i);

    void showSearchHistory();

    void addToHistory(Task task);

    void createTask(String name, String description);

    void createSubtask(String name, String description);

    void createEpic(String name, String description);

}
