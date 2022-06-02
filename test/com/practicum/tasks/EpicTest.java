package com.practicum.tasks;

import com.practicum.managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {

    private static InMemoryTaskManager tm1;

    @BeforeEach
    public void beforeEach() {
        tm1 = new InMemoryTaskManager();
    }

    @Test
    public void statusOfEmptyEpicShouldBeNew() {
        tm1.createEpic("Test Epic", "Description", 0,"01.01.1970 00:00");
        String curStatus = tm1.epicMap.get(0).getStatus();
        Assertions.assertEquals("новое", curStatus);
    }

    @Test
    public void statusOfEpicWithSameSubsShouldBeNew() {
        tm1.createEpic("Test Epic", "Description", 0,"01.01.1970 00:00");
        tm1.createSubtask("test sub", "desc2", 2,"01.01.2020 00:00");
        tm1.createSubtask("test sub2", "desc3", 2,"02.01.2020 00:00");
        String curStatus = tm1.epicMap.get(0).getStatus();
        Assertions.assertEquals("новое", curStatus);
    }

    @Test
    public void statusOfEpicWithSameSubsShouldBeDone() {
        tm1.createEpic("Test Epic", "Description", 0,"01.01.1970 00:00");
        tm1.createSubtask("test sub", "desc2", 2,"01.01.2020 00:00");
        tm1.createSubtask("test sub2", "desc3", 2,"02.01.2020 00:00");
        tm1.updateTask(1, "выполнено");
        tm1.updateTask(2, "выполнено");
        String curStatus = tm1.epicMap.get(0).getStatus();
        Assertions.assertEquals("выполнено", curStatus);
    }

    @Test
    public void statusOfEpicWithDifferentSubsShouldBeInProgress() {
        tm1.createEpic("Test Epic", "Description", 0,"01.01.1970 00:00");
        tm1.createSubtask("test sub", "desc2", 2,"01.01.2020 00:00");
        tm1.createSubtask("test sub2", "desc3", 2,"02.01.2020 00:00");
        tm1.updateTask(2, "выполнено");
        String curStatus = tm1.epicMap.get(0).getStatus();
        Assertions.assertEquals("делаю", curStatus);
    }

    @Test
    public void statusOfEpicWithSubInProgressShouldBeInProgress() {
        tm1.createEpic("Test Epic", "Description", 0,"01.01.1970 00:00");
        tm1.createSubtask("test sub", "desc2", 2,"01.01.2020 00:00");
        tm1.createSubtask("test sub2", "desc3", 2,"02.01.2020 00:00");
        tm1.updateTask(2, "делаю");
        String curStatus = tm1.epicMap.get(0).getStatus();
        Assertions.assertEquals("делаю", curStatus);
    }

}