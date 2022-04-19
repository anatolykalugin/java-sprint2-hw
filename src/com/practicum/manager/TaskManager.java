package com.practicum.manager;

import com.practicum.tasks.Task;

public interface TaskManager {

    void updateTask(int id, String status);

    void searchAndDelete(int i);

    void showSubs(int i);

    void printMenu();

    Task searchForTask(int i);

    void showEverything();

    void createSomething(int i, int id);

    void clearCategory(int i);

    void showSearchHistory();

    void addToHistory(Task task);

}
