package com.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.managers.adapters.DurationAdapter;
import com.practicum.managers.adapters.LDTTypeAdapter;
import com.practicum.managers.http.HttpTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LDTTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
