package com.practicum.managers;

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

    void createTask(String name, String description, long duration, String date);

    void createSubtask(String name, String description, long duration, String date);

    void createEpic(String name, String description, long duration, String date);

    void addDurationAndDate(Task task, long duration, String date);

    void addEpicDuration(long duration);

    void addEpicStartTime(String startDate);

    void addEpicEndTime();

    void addToTree(Task task);

    void getPrioritizedTasks();

}
