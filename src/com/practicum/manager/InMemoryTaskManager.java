package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();
    public HashMap<Integer, Subtask> subMap = new HashMap<>();
    public HashMap<Epic, List<Subtask>> epicTasks = new HashMap<>();

    @Override
    public void printMenu() {
        System.out.println("Введите нужную цифру");
        System.out.println("1 - Создать новую задачу");
        System.out.println("2 - Создать новый эпик с подзадачами");
        System.out.println("3 - Просмотреть все задачи по категориям");
        System.out.println("4 - Удалить все задачи в категории");
        System.out.println("5 - Найти задачу по идентификатору");
        System.out.println("6 - Удалить задачу по идентификатору");
        System.out.println("7 - Обновить задачу по идентификатору");
        System.out.println("8 - Просмотреть подзадачи конкретного эпика");
        System.out.println("9 - Просмотреть историю поиска задач");
        System.out.println("0 - Выйти из программы");
    }

    @Override
    public void updateTask(int id, String status) {
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

    public Epic searchForEpic(Subtask subtask) {
        for (Epic key : epicTasks.keySet()) {
            if (epicTasks.get(key).contains(subtask)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public void showEverything() {
        System.out.println("Список задач:");
        for (Integer i : taskMap.keySet()) {
            System.out.println(taskMap.get(i));
        }
        System.out.println("Список эпиков:");
        for (Integer i : epicMap.keySet()) {
            System.out.println(epicMap.get(i));
        }
        System.out.println("Список подзадач:");
        for (Integer i : subMap.keySet()) {
            System.out.println(subMap.get(i));
        }
    }

    @Override
    public Object searchForTask(int i) {
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

    @Override
    public void searchAndDelete(int i) {
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

    @Override
    public void showSubs(int i) {
        if (epicTasks.containsKey((Epic) searchForTask(i))) {
            System.out.println(epicTasks.get((Epic) searchForTask(i)));
        }
    }

    @Override
    public void createSomething(int i, int id) {
        if (i == 1) {
            Task task = Task.createTask(id, Status.NEW.getStatus());
            taskMap.put(id, task);
        } else if (i == 2) {
            Epic epic = Epic.createEpic(id, Status.NEW.getStatus());
            epicMap.put(id, epic);
        } else if (i == 3) {
            Subtask subtask = Subtask.createSubtask(id, Status.NEW.getStatus());
            subMap.put(id, subtask);
            List<Subtask> subArray = new ArrayList<>();
            int neededKey = 0;
            for (int o : epicMap.keySet()) {
                if (o > neededKey) {
                    neededKey = o;
                }
            }
            Epic epicToHelp = epicMap.get(neededKey);
            if (epicTasks.containsKey(epicToHelp)) {
                subArray = epicTasks.get(epicToHelp);
            }
            subArray.add(subtask);
            epicTasks.put(epicToHelp, subArray);
        }
    }

    @Override
    public void clearCategory(int i) {
        if (i == 1) {
            taskMap.clear();
        } else if (i == 2) {
            epicMap.clear();
            subMap.clear();
            epicTasks.clear();
        } else if (i == 3) {
            subMap.clear();
            epicTasks.clear();
        }
    }

}
