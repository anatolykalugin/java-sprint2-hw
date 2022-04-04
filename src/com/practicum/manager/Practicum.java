package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Practicum {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = Managers.getDefault();

        int id = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            taskManager.printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    Task task = Task.createTask(id, Status.NEW.getStatus());
                    taskManager.taskMap.put(id, task);
                    id++;
                    break;
                case 2:
                    Epic epic = Epic.createEpic(id, Status.NEW.getStatus());
                    taskManager.epicMap.put(id, epic);
                    id++;
                    while (true) {
                        System.out.println("Хотите добавить подзадачу? 1 - Да, 0 - Нет");
                        int command2 = scanner.nextInt();
                        if (command2 == 1) {
                            Subtask subtask = Subtask.createSubtask(id, Status.NEW.getStatus());
                            taskManager.subMap.put(id, subtask);
                            List<Subtask> subArray = new ArrayList<>();
                            if (taskManager.epicTasks.containsKey(epic)) {
                                subArray = taskManager.epicTasks.get(epic);
                            }
                            subArray.add(subtask);
                            taskManager.epicTasks.put(epic, subArray);
                            id++;
                        } else if (command2 == 0) {
                            break;
                        } else {
                            System.out.println("Такой опции нет.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Список задач:");
                    taskManager.showEveryTask(taskManager.taskMap);
                    System.out.println("Список эпиков:");
                    taskManager.showEveryEpic(taskManager.epicMap);
                    System.out.println("Список подзадач:");
                    taskManager.showEverySub(taskManager.subMap);
                    break;
                case 4:
                    System.out.println("Какую категорию вы хотите очистить?");
                    System.out.println("1 - задачи, 2 - эпики с подзадачами, 3 - только подзадачи");
                    int command3 = scanner.nextInt();
                    if (command3 == 1) {
                        taskManager.taskMap.clear();
                    } else if (command3 == 2) {
                        taskManager.epicMap.clear();
                        taskManager.subMap.clear();
                        taskManager.epicTasks.clear();
                    } else if (command3 == 3) {
                        taskManager.subMap.clear();
                        taskManager.epicTasks.clear();
                    } else if (command3 == 0) {
                        break;
                    } else {
                        System.out.println("Такой опции нет.");
                    }
                    break;
                case 5:
                    System.out.println("Введите идентификатор задачи:");
                    int command4 = scanner.nextInt();
                    if (taskManager.searchForTask(command4) == null) {
                        System.out.println("Нет задачи с таким идентификатором");
                    } else {
                        System.out.println(taskManager.searchForTask(command4));
                        taskManager.historyManager.addHistory(taskManager.searchForTask(command4));
                    }
                    break;
                case 6:
                    System.out.println("Введите идентификатор задачи на удаление:");
                    int command5 = scanner.nextInt();
                    taskManager.searchAndDelete(command5);
                    break;
                case 7:
                    System.out.println("Введите идентификатор");
                    int i = scanner.nextInt();
                    if (taskManager.searchForTask(i) == null) {
                        System.out.println("Нет задачи с таким идентификатором");
                    } else {
                        System.out.println("Введите обновленный статус (новое / делаю / выполнено):");
                        String newStatus = scanner.next().toLowerCase();
                        if (newStatus.equals(Status.NEW.getStatus())) {
                            taskManager.updateTask(i, Status.NEW.getStatus());
                        } else if (newStatus.equals(Status.IN_PROGRESS.getStatus())) {
                            taskManager.updateTask(i, Status.IN_PROGRESS.getStatus());
                        } else if ((newStatus.equals(Status.DONE.getStatus()))) {
                            taskManager.updateTask(i, Status.DONE.getStatus());
                        } else {
                            System.out.println("Введен неверный статус. Попробуйте снова");
                            break;
                        }
                    }
                    break;
                case 8:
                    System.out.println("Введите идентификатор эпика:");
                    int command6 = scanner.nextInt();
                    taskManager.showSubs(command6);
                    break;
                case 9:
                    taskManager.historyManager.showHistory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Такой опции нет. Попробуйте снова.");
            }
        }
    }
}
