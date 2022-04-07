package com.practicum.manager;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{

    public LinkedList<Object> searchHistory = new LinkedList<>();

    @Override
    public void addHistory(Object obj) {
        if (searchHistory.size() >= 10) {
            searchHistory.removeFirst();
        }
        searchHistory.add(obj);
    }

    @Override
    public void showHistory() {
        System.out.println(searchHistory);
    }
}
