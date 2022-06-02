package com.practicum.managers;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    @Override
    @BeforeEach
    public void beforeEach() {
        tm = new FileBackedTasksManager("resources/stuff.csv");
        super.beforeEach();
    }

    @Test
    public void shouldSaveEmptyTasksAndEmptyHistoryAndLoadEmpty() {
        tm.createTask("t1", "d1", 10, "01.01.2010 10:10");
        tm.searchAndDelete(0);
        FileBackedTasksManager fb = FileBackedTasksManager.loadFromFile("resources/stuff.csv");
        Assertions.assertNull(fb.searchForTask(0));
        Assertions.assertEquals(List.of(), fb.searchHistory.getHistory());
    }

    @Test
    public void shouldSaveEpicWithoutSubsAndLoadIt() {
        tm.createEpic("e1", "e1", 0, "01.01.1970 00:00");
        Epic epicToCheck = (Epic) tm.searchForTask(4);
        FileBackedTasksManager fb = FileBackedTasksManager.loadFromFile("resources/stuff.csv");
        Assertions.assertEquals(epicToCheck, fb.searchForTask(4));
    }

    @Test
    public void shouldSaveSomeTasksAndHistoryAndLoadThem() {
        tm.createTask("t1", "d1", 10, "01.01.2010 10:10");
        tm.createEpic("e1", "e1", 0, "01.01.1970 00:00");
        tm.createSubtask("t1", "d1", 10, "02.01.2010 10:10");
        tm.addEpicDuration(10);
        tm.addEpicStartTime("02.01.2010 10:10");
        tm.addEpicEndTime();
        Task taskToCheck = tm.searchForTask(1);
        Epic epicToCheck = (Epic) tm.searchForTask(2);
        Subtask subToCheck = (Subtask) tm.searchForTask(3);
        tm.addToHistory(tm.searchForTask(2));
        tm.addToHistory(tm.searchForTask(1));
        tm.addToHistory(tm.searchForTask(3));
        tm.addToHistory(tm.searchForTask(2));
        FileBackedTasksManager fb = FileBackedTasksManager.loadFromFile("resources/stuff.csv");
        Assertions.assertNotNull(fb.searchForTask(1));
        Assertions.assertNotNull(fb.searchForTask(2));
        Assertions.assertNotNull(fb.searchForTask(3));
        Assertions.assertEquals(taskToCheck, fb.searchForTask(1));
        Assertions.assertEquals(epicToCheck, fb.searchForTask(2));
        Assertions.assertEquals(subToCheck, fb.searchForTask(3));
        List<Task> resultList = fb.searchHistory.getHistory();
        List<Task> listToCheck = List.of(taskToCheck, subToCheck, epicToCheck);
        Assertions.assertEquals(listToCheck, resultList, "История не совпадает");
    }

}