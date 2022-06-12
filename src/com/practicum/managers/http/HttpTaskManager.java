package com.practicum.managers.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.practicum.managers.FileBackedTasksManager;
import com.practicum.managers.Managers;

import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    Managers manag = new Managers();

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super();
        gson = manag.getGson();
        this.kvTaskClient = new KVTaskClient(url);
        restoreManagerStatus();
    }

    // TODO: починить метод ниже + старт и стоп серверов + тесты
    private void restoreManagerStatus() {
        JsonElement allTasksArray = JsonParser.parseString(kvTaskClient.load("tasks/task"));
        JsonElement allEpicsInJson = JsonParser.parseString(kvTaskClient.load("tasks/epic"));
        JsonElement allSubsInJson = JsonParser.parseString(kvTaskClient.load("tasks/subtask"));
        JsonElement historyInJson = JsonParser.parseString(kvTaskClient.load("tasks/history"));
    }

    @Override
    protected void save() {
        super.save();
        kvTaskClient.put("tasks/task", gson.toJson(taskMap));
        kvTaskClient.put("tasks/epic", gson.toJson(epicMap));
        kvTaskClient.put("tasks/subtask", gson.toJson(subMap));
        kvTaskClient.put("tasks/history", gson.toJson(showSearchHistory()));
    }

}
