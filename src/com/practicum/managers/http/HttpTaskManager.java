package com.practicum.managers.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.practicum.exceptions.ManagerSaveException;
import com.practicum.managers.FileBackedTasksManager;
import com.practicum.managers.Managers;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    Managers manager1 = new Managers();

    public HttpTaskManager(String url) {
        super();
        gson = manager1.getGson();
        this.kvTaskClient = new KVTaskClient(url);
        restoreManagerStatus();
    }

    private void restoreManagerStatus() {
        try {
            String tasksFromJson = kvTaskClient.load("tasks/task");
            if (tasksFromJson != null) {
                taskMap = gson.fromJson(tasksFromJson, new TypeToken<HashMap<Integer, Task>>() {
                }.getType());
                for (Task t : taskMap.values()) {
                    addToTree(t);
                }
            }
            String epicsFromJson = kvTaskClient.load("tasks/epic");
            if (epicsFromJson != null) {
                epicMap = gson.fromJson(epicsFromJson, new TypeToken<HashMap<Integer, Epic>>() {
                }.getType());
            }
            String subsFromJson = kvTaskClient.load("tasks/subtask");
            if (subsFromJson != null) {
                subMap = gson.fromJson(subsFromJson, new TypeToken<HashMap<Integer, Subtask>>() {
                }.getType());
                for (Subtask t : subMap.values()) {
                    addToTree(t);
                   List<Subtask> subsList = new ArrayList<>();
                    if (epicTasks.containsKey((Epic) searchForTask(t.getEpicLinkId()))) {
                        subsList = epicTasks.get((Epic) searchForTask(t.getEpicLinkId()));
                    }
                    subsList.add(t);
                    epicTasks.put((Epic) searchForTask(t.getEpicLinkId()), subsList);
                }
            }
            String historyFromJson = kvTaskClient.load("tasks/history");
            if (historyFromJson != null) {
                List<Task> recreatedHistory = gson.fromJson(historyFromJson, new TypeToken<List<Task>>() {
                }.getType());
                for (Task t : recreatedHistory) {
                    addToHistory(t);
                }
            }
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка в загрузке");
        }
    }

    @Override
    protected void save() {
        try {
            kvTaskClient.put("tasks/task", gson.toJson(taskMap));
            kvTaskClient.put("tasks/epic", gson.toJson(epicMap));
            kvTaskClient.put("tasks/subtask", gson.toJson(subMap));
            kvTaskClient.put("tasks/history", gson.toJson(showSearchHistory()));
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка в сохранении");
        }
    }
}
