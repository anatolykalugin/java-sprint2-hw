package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Manager {

    static HashMap<Integer, Task> taskMap = new HashMap<>();
    static HashMap<Integer, Epic> epicMap = new HashMap<>();
    static HashMap<Integer, Subtask> subMap = new HashMap<>();
    static HashMap<Epic, List<Subtask>> epicTasks = new HashMap<>();

    public static void main(String[] args) {

        int id = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    Task task = Task.createTask(id, Status.NEW.getStatus());
                    taskMap.put(id, task);
                    id++;
                    break;
                case 2:
                    Epic epic = Epic.createEpic(id, Status.NEW.getStatus());
                    epicMap.put(id, epic);
                    id++;
                    while (true) {
                        System.out.println("Хотите добавить подзадачу? 1 - Да, 0 - Нет");
                        int command2 = scanner.nextInt();
                        if (command2 == 1) {
                            Subtask subtask = Subtask.createSubtask(id, Status.NEW.getStatus());
                            subMap.put(id, subtask);
                            List<Subtask> subArray = new ArrayList<>();
                            if (epicTasks.containsKey(epic)) {
                                subArray = epicTasks.get(epic);
                            }
                            subArray.add(subtask);
                            epicTasks.put(epic, subArray);
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
                    showEveryTask(taskMap);
                    System.out.println("Список эпиков:");
                    showEveryEpic(epicMap);
                    System.out.println("Список подзадач:");
                    showEverySub(subMap);
                    break;
                case 4:
                    System.out.println("Какую категорию вы хотите очистить?");
                    System.out.println("1 - задачи, 2 - эпики с подзадачами, 3 - только подзадачи");
                    int command3 = scanner.nextInt();
                    if (command3 == 1) {
                        taskMap.clear();
                    } else if (command3 == 2) {
                        epicMap.clear();
                        subMap.clear();
                        epicTasks.clear();
                    } else if (command3 == 3) {
                        subMap.clear();
                        epicTasks.clear();
                    } else if (command3 == 0) {
                        break;
                    } else {
                        System.out.println("Такой опции нет.");
                    }
                    break;
                case 5:
                    System.out.println("Введите идентификатор задачи:");
                    int command4 = scanner.nextInt();
                    if (searchForTask(command4) == null) {
                        System.out.println("Нет задачи с таким идентификатором");
                    } else {
                        System.out.println(searchForTask(command4));
                    }
                    break;
                case 6:
                    System.out.println("Введите идентификатор задачи на удаление:");
                    int command5 = scanner.nextInt();
                    searchAndDelete(command5);
                    break;
                case 7:
                    System.out.println("Введите идентификатор");
                    int i = scanner.nextInt();
                    if (searchForTask(i) == null) {
                        System.out.println("Нет задачи с таким идентификатором");
                    } else {
                        System.out.println("Введите обновленный статус (новое / делаю / выполнено):");
                        String newStatus = scanner.next().toLowerCase();
                        if (newStatus.equals(Status.NEW.getStatus())) {
                            updateTask(i, Status.NEW.getStatus());
                        } else if (newStatus.equals(Status.IN_PROGRESS.getStatus())) {
                            updateTask(i, Status.IN_PROGRESS.getStatus());
                        } else if ((newStatus.equals(Status.DONE.getStatus()))) {
                            updateTask(i, Status.DONE.getStatus());
                        } else {
                            System.out.println("Введен неверный статус. Попробуйте снова");
                            break;
                        }
                    }
                    break;
                case 8:
                    System.out.println("Введите идентификатор эпика:");
                    int command6 = scanner.nextInt();
                    showSubs(command6);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Такой опции нет. Попробуйте снова.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("Введите нужную цифру");
        System.out.println("1 - Создать новую задачу");
        System.out.println("2 - Создать новый эпик с подзадачами");
        System.out.println("3 - Просмотреть все задачи по категориям");
        System.out.println("4 - Удалить все задачи в категории");
        System.out.println("5 - Найти задачу по идентификатору");
        System.out.println("6 - Удалить задачу по идентификатору");
        System.out.println("7 - Обновить задачу по идентификатору");
        System.out.println("8 - Просмотреть подзадачи конкретного эпика");
        System.out.println("0 - Выйти из программы");
    }

    public static void updateTask(int id, String status) {
        if (taskMap.containsKey(id)) {
            taskMap.put(id, Task.createTask(id, status));
        } else if (epicMap.containsKey(id)) {
            List<Subtask> subArray = epicTasks.get((Epic) searchForTask(id));
            Epic epic = Epic.createEpic(id, status);
            epicMap.put(id, epic);
            epicTasks.remove((Epic) searchForTask(id));
            epicTasks.put(epic, subArray);
        } else if (subMap.containsKey(id)) {
            Subtask subToReplace = (Subtask) searchForTask(id);
            Subtask subNew = Subtask.createSubtask(id,status);
            subMap.put(id, subNew);
            if (searchForEpic(subToReplace) != null) {
                Epic epic1 = searchForEpic(subToReplace);
                List<Subtask> subArray2 = epicTasks.get(searchForEpic(subToReplace));
                subArray2.add(subNew);
                subArray2.remove(subToReplace);
                boolean isEqual = true;
                for (Subtask obj : subArray2) {
                    if (!(obj).getStatus().equals(status)) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    epic1.setStatus(status);
                } else {
                    epic1.setStatus(Status.IN_PROGRESS.getStatus());
                }
                epicTasks.remove(epic1);
                epicTasks.put(epic1, subArray2);
            }
        }
    }

    public static Epic searchForEpic(Subtask subtask) {
        for (Epic key : epicTasks.keySet()) {
            if (epicTasks.get(key).contains(subtask)) {
                return key;
            }
        }
        return null;
    }

    public static void showEveryTask(HashMap<Integer, Task> taskList) {
        for (Integer i : taskList.keySet()) {
            System.out.println(taskList.get(i));
        }
    }

    public static void showEveryEpic(HashMap<Integer, Epic> epicList) {
        for (Integer i : epicList.keySet()) {
            System.out.println(epicList.get(i));
        }
    }

    public static void showEverySub(HashMap<Integer, Subtask> subList) {
        for (Integer i : subList.keySet()) {
            System.out.println(subList.get(i));
        }
    }

    public static Object searchForTask(int i) {
        if (i >= 0 && taskMap.containsKey(i)) {
            return taskMap.get(i);
        } else if (i >= 0 && epicMap.containsKey(i)) {
            return epicMap.get(i);
        } else if (i >= 0 && subMap.containsKey(i)) {
            return subMap.get(i);
        } else {
            return null;
        }
    }

    public static void searchAndDelete(int i) {
        if (i >= 0 && taskMap.containsKey(i)) {
            taskMap.remove(i);
        } else if (i >= 0 && epicMap.containsKey(i)) {
            epicMap.remove(i);
        } else if (i >= 0 && subMap.containsKey(i)) {
            subMap.remove(i);
        } else {
            System.out.println("Нет задачи с таким идентификатором");
        }
    }

    public static void showSubs(int i) {
        if (epicTasks.containsKey((Epic) searchForTask(i))) {
            System.out.println(epicTasks.get((Epic) searchForTask(i)));
        }
    }
}
