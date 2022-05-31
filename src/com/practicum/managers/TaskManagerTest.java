package com.practicum.managers;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskManagerTest<T extends TaskManager> {

    private T tm;

    @BeforeEach
    public void beforeEach() {
        tm = (T) new InMemoryTaskManager();
    }

    @AfterEach
    public void afterEach() {
        tm.clearCategory(1);
        tm.clearCategory(2);
        tm.clearCategory(3);
    }

    @Test
    void shouldUpdateTask() {
        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        tm.updateTask(0, "выполнено");
        Task task = tm.searchForTask(0);
        Assertions.assertEquals(Status.DONE.getStatus(), task.getStatus());
    }

    @Test
    void shouldSearchAndDelete() {
        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        tm.searchAndDelete(0);
        Assertions.assertNull(tm.searchForTask(0));
    }

    @Test
    void shouldSearchForTask() {
        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        Assertions.assertNotNull(tm.searchForTask(0));
    }

    @Test
    void shouldClearCategory() {
        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        tm.createTask("t2", "d2", 10, "02.01.2010 00:00");
        tm.clearCategory(1);
        Assertions.assertNull(tm.searchForTask(0));
        Assertions.assertNull(tm.searchForTask(1));
    }

    @Test
    void shouldCreateTaskAndAddDurationAndDate() {
        Task taskToTest = new Task("t1", "d1", 0, "новое", TaskTypes.TASK);
        taskToTest.setStartTime(LocalDateTime.of(2010,1,1,0,0));
        taskToTest.setDuration(Duration.ofMinutes(10));

        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        Task task = tm.searchForTask(0);
        LocalDateTime endTime = task.getEndTime();
        Assertions.assertNotNull(task, "Task not found");
        Assertions.assertEquals(taskToTest, task, "Tasks are not equal");
        Assertions.assertEquals(taskToTest.getStartTime(), task.getStartTime());
        Assertions.assertEquals(taskToTest.getDuration(), task.getDuration());
        Assertions.assertEquals(LocalDateTime.of(2010,1,1,0,10),
                endTime, "End times are not equal");
    }

    @Test
    void shouldCreateEpicAndSubtaskAndAddEpicDurationAndDateAndCheckEpicsEndTime() {
        Epic epicToTest = new Epic("e1", "e1", 0, "новое", TaskTypes.EPIC);
        Subtask subToTest = new Subtask("s1", "s1", 1, "новое",
                TaskTypes.SUBTASK, 0);
        epicToTest.setStartTime(LocalDateTime.of(2010,1,1,0,0));
        epicToTest.setDuration(Duration.ofMinutes(10));
        epicToTest.setEndDate(LocalDateTime.of(2010,1,1,0,10));
        subToTest.setStartTime(LocalDateTime.of(2010,1,1,0,0));
        subToTest.setDuration(Duration.ofMinutes(10));

        tm.createEpic("e1", "e1", 0, "01.01.1970 00:00");
        tm.createSubtask("s1", "s1", 10, "01.01.2010 00:00");
        tm.addEpicDuration(10);
        tm.addEpicStartTime("01.01.2010 00:00");
        tm.addEpicEndTime();
        Epic epic = (Epic) tm.searchForTask(0);
        Subtask sub = (Subtask) tm.searchForTask(1);
        Assertions.assertNotNull(epic, "Epic not found");
        Assertions.assertNotNull(sub, "Sub not found");
        Assertions.assertEquals(epicToTest, epic, "Epics are not equal");
        Assertions.assertEquals(subToTest, sub, "Subs are not equal");
        Assertions.assertEquals(epicToTest.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(epicToTest.getDuration(), epic.getDuration());
        Assertions.assertEquals(LocalDateTime.of(2010,1,1,0,10),
                epic.getEndDate(), "End times are not equal");
        Assertions.assertEquals(subToTest.getStartTime(), sub.getStartTime());
        Assertions.assertEquals(subToTest.getDuration(), sub.getDuration());
        Assertions.assertEquals(LocalDateTime.of(2010,1,1,0,10),
                sub.getEndTime(), "End times are not equal");
    }

    @Test
    void addToTreeAndCheckValidity() {
        Task taskToTest1 = new Task("t1", "d1", 0, "новое", TaskTypes.TASK);
        taskToTest1.setStartTime(LocalDateTime.of(2010,1,1,0,0));
        taskToTest1.setDuration(Duration.ofMinutes(10));
        Task taskToTest2 = new Task("should add", "d3", 1, "новое", TaskTypes.TASK);
        taskToTest2.setStartTime(LocalDateTime.of(2010,1,2,0,0));
        taskToTest2.setDuration(Duration.ofMinutes(10));

        tm.createTask("t1", "d1", 10, "01.01.2010 00:00");
        tm.createTask("shouldn't add", "d2", 10, "01.01.2010 00:01");
        tm.createTask("should add", "d3", 10, "02.01.2010 00:00");

        Assertions.assertEquals(taskToTest1, tm.searchForTask(0));
        Assertions.assertEquals(taskToTest2, tm.searchForTask(1));
        Assertions.assertNull(tm.searchForTask(2));
    }
}