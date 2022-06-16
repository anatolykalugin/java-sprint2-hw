package com.practicum.managers;

import com.practicum.kvserver.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager taskManager = new Managers().getDefault();
        taskManager.createTask("Task1", "Description1", 10, "10.06.2022 10:00");
        taskManager.createTask("Task1", "Description2", 15, "10.06.2022 10:11");
        taskManager.createEpic("Epic1", "Description3", 0, "01.01.1970 00:00");
        taskManager.createSubtask("Sub1", "Description4", 20, "11.06.2022 09:43");
        taskManager.createSubtask("Sub2", "Description5", 30, "09.06.2022 23:24");
        taskManager.addToHistory(taskManager.searchForTask(0));
        taskManager.addToHistory(taskManager.searchForTask(4));
        taskManager.addToHistory(taskManager.searchForTask(2));
        taskManager.addToHistory(taskManager.searchForTask(4));
        taskManager.addToHistory(taskManager.searchForTask(1));
        taskManager.addToHistory(taskManager.searchForTask(0));
        // До восстановления:
        System.out.println("Сабы эпика: \n" + taskManager.showSubs(2));
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("Изначальная история:\n" + taskManager.showSearchHistory());
        TaskManager tm2 = new Managers().getDefault();
        // После восстановления:
        System.out.println("Сабы эпика: \n" + tm2.showSubs(2));
        System.out.println(tm2.getPrioritizedTasks());
        System.out.println("Восстановленная история: \n" + tm2.showSearchHistory());

        kvServer.stop();
    }
}