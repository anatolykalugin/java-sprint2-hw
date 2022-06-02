package com.practicum.managers;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class HistoryManagerTest {

    private InMemoryTaskManager tm;

    @BeforeEach
    public void beforeEach() {
        tm = new InMemoryTaskManager();
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
    }

    @AfterEach
    public void afterEach() {
        tm.clearCategory(1);
        tm.clearCategory(2);
        tm.clearCategory(3);
    }

    @Test
    void shouldAddWhenEmptyAndGetHistory() {
        Task task = tm.taskMap.get(0);
        tm.searchHistory.add(task);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(1, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task, tList.get(0));
    }

    @Test
    void shouldAddWhenDuplicateAndGetHistory() {
        Task task = tm.taskMap.get(0);
        tm.searchHistory.add(task);
        tm.searchHistory.add(task);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(1, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task, tList.get(0));
    }

    @Test
    void shouldRemoveFirstAndGetHistory() {
        tm.createEpic("t2", "d2", 2,"02.01.2020 00:00");
        tm.createSubtask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Epic task1 = tm.epicMap.get(1);
        Subtask task2 = tm.subMap.get(2);
        tm.searchHistory.add(task0);
        tm.searchHistory.add(task1);
        tm.searchHistory.add(task2);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(3, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task0, tList.get(0));
        Assertions.assertEquals(task1, tList.get(1));
        Assertions.assertEquals(task2, tList.get(2));
        tm.searchHistory.remove(0);
        tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(2, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task1, tList.get(0));
        Assertions.assertEquals(task2, tList.get(1));
    }

    @Test
    void shouldRemoveMidAndGetHistory() {
        tm.createEpic("t2", "d2", 2,"02.01.2020 00:00");
        tm.createSubtask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Epic task1 = tm.epicMap.get(1);
        Subtask task2 = tm.subMap.get(2);
        tm.searchHistory.add(task0);
        tm.searchHistory.add(task1);
        tm.searchHistory.add(task2);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(3, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task0, tList.get(0));
        Assertions.assertEquals(task1, tList.get(1));
        Assertions.assertEquals(task2, tList.get(2));
        tm.searchHistory.remove(1);
        tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(2, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task0, tList.get(0));
        Assertions.assertEquals(task2, tList.get(1));
    }

    @Test
    void shouldRemoveLastAndGetHistory() {
        tm.createEpic("t2", "d2", 2,"02.01.2020 00:00");
        tm.createSubtask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Epic task1 = tm.epicMap.get(1);
        Subtask task2 = tm.subMap.get(2);
        tm.searchHistory.add(task0);
        tm.searchHistory.add(task1);
        tm.searchHistory.add(task2);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(3, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task0, tList.get(0));
        Assertions.assertEquals(task1, tList.get(1));
        Assertions.assertEquals(task2, tList.get(2));
        tm.searchHistory.remove(2);
        tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(2, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task0, tList.get(0));
        Assertions.assertEquals(task1, tList.get(1));
    }

    @Test
    void shouldContainTask() {
        Task task = tm.taskMap.get(0);
        tm.searchHistory.add(task);
        boolean testBoolean = tm.searchHistory.containsTask(0);
        Assertions.assertTrue(testBoolean, "Неверное содержание мапы с нодами");
    }
}