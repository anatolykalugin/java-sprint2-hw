package com.practicum.manager;

public interface TaskManager {

    void updateTask(int id, String status);

    void searchAndDelete(int i);

    void showSubs(int i);

    void printMenu();

    Object searchForTask(int i);

}