package com.practicum.manager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    static List<Object> searchHistory = new ArrayList<>();

    @Override
    public void addHistory(Object obj) {
        if (searchHistory.size() >= 10) {
            searchHistory.remove(0);
        }
        searchHistory.add(obj);
    }

    @Override
    public void showHistory() {
        System.out.println(searchHistory);
    }
}
