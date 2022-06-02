package com.practicum.managers;

import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void beforeEach() {
        tm = new InMemoryTaskManager();
        super.beforeEach();
    }

    @Override
    @Test
    void shouldSearchForTask() {
        Task taskToCheck = new Task("t1", "d1", 0, "новое", TaskTypes.TASK);
        Assertions.assertNotNull(tm.searchForTask(0));
        Assertions.assertEquals(taskToCheck, tm.searchForTask(0));
    }

}

