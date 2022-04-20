package com.practicum.manager;

public class Practicum {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        int id = 0;

//      тест согласно условиям ТЗ:

        taskManager.createSomething(1, id);
        id++;
        taskManager.createSomething(1, id);
        id++;
        taskManager.createSomething(2, id);
        id++;
        taskManager.createSomething(3, id);
        id++;
        taskManager.createSomething(3, id);
        id++;
        taskManager.createSomething(3, id);
        id++;
        taskManager.createSomething(2, id);

        System.out.println(taskManager.searchForTask(0));
        taskManager.addToHistory(taskManager.searchForTask(0));
        System.out.println(taskManager.searchForTask(2));
        taskManager.addToHistory(taskManager.searchForTask(2));
        System.out.println(taskManager.searchForTask(5));
        taskManager.addToHistory(taskManager.searchForTask(5));
        System.out.println(taskManager.searchForTask(5));
        taskManager.addToHistory(taskManager.searchForTask(5));
        System.out.println(taskManager.searchForTask(4));
        taskManager.addToHistory(taskManager.searchForTask(4));
        System.out.println(taskManager.searchForTask(1));
        taskManager.addToHistory(taskManager.searchForTask(1));
        System.out.println(taskManager.searchForTask(3));
        taskManager.addToHistory(taskManager.searchForTask(3));
        System.out.println(taskManager.searchForTask(1));
        taskManager.addToHistory(taskManager.searchForTask(1));
        System.out.println(taskManager.searchForTask(0));
        taskManager.addToHistory(taskManager.searchForTask(0));

        System.out.println("История поиска до удалений: ");
        taskManager.showSearchHistory();

        System.out.println("Подзадачи эпика с ID 2: ");
        taskManager.showSubs(2);

        taskManager.searchAndDelete(0);

        System.out.println("История поиска после удаления задачи 0: ");
        taskManager.showSearchHistory();

        taskManager.searchAndDelete(2);

        System.out.println("История поиска после удаления эпика 2: ");
        taskManager.showSearchHistory();

        taskManager.searchAndDelete(1);

        System.out.println("История поиска после удаления последней задачи: ");
        taskManager.showSearchHistory();

//        Ниже - взаимодействие с юзером:

//        Scanner scanner = new Scanner(System.in);

        /*while (true) {
            taskManager.printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    taskManager.createSomething(1, id);
                    id++;
                    break;
                case 2:
                    taskManager.createSomething(2, id);
                    id++;
                    while (true) {
                        System.out.println("Хотите добавить подзадачу? 1 - Да, 0 - Нет");
                        int command2 = scanner.nextInt();
                        if (command2 == 1) {
                            taskManager.createSomething(3, id);
                            id++;
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
                case 0:
                    return;
                default:
                    System.out.println("Такой опции нет. Попробуйте снова.");
            }
        }*/
    }
}
