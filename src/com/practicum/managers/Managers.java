package com.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.managers.adapters.DurationAdapter;
import com.practicum.managers.adapters.LDTTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public HistoryManager searchHistory = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return new FileBackedTasksManager("resources/stuff.csv");
    }

    public HistoryManager getDefaultHistory() {
        return searchHistory;
    }

    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LDTTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
