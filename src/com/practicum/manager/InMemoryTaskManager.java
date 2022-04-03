package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subMap = new HashMap<>();
    HashMap<Epic, List<Subtask>> epicTasks = new HashMap<>();

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

    public void showEveryTask(HashMap<Integer, Task> taskList) {
        for (Integer i : taskList.keySet()) {
            System.out.println(taskList.get(i));
        }
    }

    public void showEveryEpic(HashMap<Integer, Epic> epicList) {
        for (Integer i : epicList.keySet()) {
            System.out.println(epicList.get(i));
        }
    }

    public void showEverySub(HashMap<Integer, Subtask> subList) {
        for (Integer i : subList.keySet()) {
            System.out.println(subList.get(i));
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

}
