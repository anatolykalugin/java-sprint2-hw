package com.practicum.manager;

import com.practicum.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);
    List<Task> getHistory();
    void remove(int id);
    boolean containsTask(int id);
}
