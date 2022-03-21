package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Manager {

    static HashMap<Integer, Object> taskMap = new HashMap<>();
    static HashMap<Integer, Object> epicMap = new HashMap<>();
    static HashMap<Integer, Object> subMap = new HashMap<>();
    static HashMap<Object, List<Object>> epicTasks = new HashMap<>();

    public static void main(String[] args) {

        int id = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    Object task = Task.createTask(id, Status.NEW.getStatus());
                    taskMap.put(id, task);
                    id++;
                    break;
                case 2:
                    Object epic = Epic.createEpic(id, Status.NEW.getStatus());
                    epicMap.put(id, epic);
                    id++;
                    while (true) {
                        System.out.println("Хотите добавить подзадачу? 1 - Да, 0 - Нет");
                        int command2 = scanner.nextInt();
                        if (command2 == 1) {
                            Object subtask = Subtask.createSubtask(id, Status.NEW.getStatus());
                            subMap.put(id, subtask);
                            List<Object> subArray = new ArrayList<>();
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
                    showEverything(taskMap);
                    System.out.println("Список эпиков:");
                    showEverything(epicMap);
                    System.out.println("Список подзадач:");
                    showEverything(subMap);
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
            List<Object> objArray = epicTasks.get(searchForEpic(searchForTask(id)));
            Object epic = Epic.createEpic(id, status);
            epicMap.put(id, epic);
            epicTasks.remove(searchForEpic(searchForTask(id)));
            epicTasks.put(epic, objArray);
        } else if (subMap.containsKey(id)) {
            Object subTest = searchForTask(id);
            Object sub1 = Subtask.createSubtask(id,status);
            subMap.put(id, sub1);
            if (searchForEpic(subTest) != null) {
                List<Object> objArray = epicTasks.get(searchForEpic(subTest));
                objArray.remove(subTest);
                objArray.add(sub1);
                boolean isEqual = true;
                for (Object obj : objArray) {
                    if (!((Subtask)obj).getStatus().equals(status)) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    ((Epic)searchForEpic(sub1)).setStatus(status);
                } else {
                    ((Epic)searchForEpic(sub1)).setStatus("в процессе");
                }
            }
        }
    }

    public static Object searchForEpic(Object subtask) {
        for (Object key : epicTasks.keySet()) {
            if (epicTasks.get(key).contains(subtask)) {
                return key;
            }
        }
        return null;
    }

    public static void showEverything(HashMap<Integer, Object> taskList) {
        for (Integer i : taskList.keySet()) {
            System.out.println(taskList.get(i));
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
        if (epicTasks.containsKey(searchForTask(i))) {
            System.out.println(epicTasks.get(searchForTask(i)));
        }
    }
}
