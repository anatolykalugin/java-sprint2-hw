package com.practicum.managers;

import java.util.Scanner;

public class Practicum {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            taskManager.printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    System.out.println("Введите название задачи:");
                    String name = scanner.next();
                    System.out.println("Введите описание задачи:");
                    String description = scanner.next();
                    System.out.println("Длительность в минутах");
                    long duration = scanner.nextLong();
                    System.out.println("Срок в формате дд.мм.гггг чч:мм");
                    scanner.nextLine();
                    String date = scanner.nextLine();
                    taskManager.createTask(name, description, duration, date);
                    break;
                case 2:
                    System.out.println("Введите название эпика:");
                    String name2 = scanner.next();
                    System.out.println("Введите описание эпика:");
                    String description2 = scanner.next();
                    taskManager.createEpic(name2, description2, 0, "01.01.1970 00:00");
                    while (true) {
                        System.out.println("Хотите добавить подзадачу? 1 - Да, 0 - Нет");
                        int command2 = scanner.nextInt();
                        if (command2 == 1) {
                            System.out.println("Введите название подзадачи:");
                            String name3 = scanner.next();
                            System.out.println("Введите описание подзадачи:");
                            String description3 = scanner.next();
                            System.out.println("Длительность в минутах");
                            long durationOfSub = scanner.nextLong();
                            System.out.println("Срок в формате дд.мм.гггг чч:мм");
                            scanner.nextLine();
                            String dateOfSub = scanner.nextLine();
                            taskManager.createSubtask(name3, description3, durationOfSub, dateOfSub);
                            taskManager.addEpicDuration(durationOfSub);
                            taskManager.addEpicStartTime(dateOfSub);
                            taskManager.addEpicEndTime();
                        } else if (command2 == 0) {
                            break;
                        } else {
                            System.out.println("Такой опции нет.");
                        }
                    }
                    break;
                case 3:
                    taskManager.showEverything();
                    break;
                case 4:
                    System.out.println("Какую категорию вы хотите очистить?");
                    System.out.println("1 - задачи, 2 - эпики с подзадачами, 3 - только подзадачи");
                    int command3 = scanner.nextInt();
                    if (command3 == 1) {
                        taskManager.clearCategory(1);
                    } else if (command3 == 2) {
                        taskManager.clearCategory(2);
                    } else if (command3 == 3) {
                        taskManager.clearCategory(3);
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
                        taskManager.addToHistory(taskManager.searchForTask(command4));
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
                    taskManager.showSearchHistory();
                    break;
                case 10:
                    taskManager.getPrioritizedTasks();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Такой опции нет. Попробуйте снова.");
            }
        }
    }
}
