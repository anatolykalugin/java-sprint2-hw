package com.practicum.managers;

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
    }

    @AfterEach
    public void afterEach() {
        tm.clearCategory(1);
    }

    @Test
    void shouldAddWhenEmptyAndGetHistory() {
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
        Task task = tm.taskMap.get(0);
        tm.searchHistory.add(task);
        List<Task> tList = tm.searchHistory.getHistory();
        Assertions.assertNotNull(tList, "История не должна быть пустой");
        Assertions.assertEquals(1, tList.size(), "Некорректный размер истории");
        Assertions.assertEquals(task, tList.get(0));
    }

    @Test
    void shouldAddWhenDuplicateAndGetHistory() {
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
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
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
        tm.createTask("t2", "d2", 2,"02.01.2020 00:00");
        tm.createTask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Task task1 = tm.taskMap.get(1);
        Task task2 = tm.taskMap.get(2);
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
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
        tm.createTask("t2", "d2", 2,"02.01.2020 00:00");
        tm.createTask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Task task1 = tm.taskMap.get(1);
        Task task2 = tm.taskMap.get(2);
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
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
        tm.createTask("t2", "d2", 2,"02.01.2020 00:00");
        tm.createTask("t3", "d3", 2,"03.01.2020 00:00");
        Task task0 = tm.taskMap.get(0);
        Task task1 = tm.taskMap.get(1);
        Task task2 = tm.taskMap.get(2);
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
        tm.createTask("t1", "d1", 2,"01.01.2020 00:00");
        Task task = tm.taskMap.get(0);
        tm.searchHistory.add(task);
        boolean testBoolean = tm.searchHistory.containsTask(0);
        Assertions.assertTrue(testBoolean, "Неверное содержание мапы с нодами");
    }
}