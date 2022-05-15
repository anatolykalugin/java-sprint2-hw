package com.practicum.manager;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager searchHistory = Managers.getDefaultHistory();

    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();
    public HashMap<Integer, Subtask> subMap = new HashMap<>();
    public HashMap<Epic, List<Subtask>> epicTasks = new HashMap<>();

    int id = 0;

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
    public void createTask(String name, String description) {
        Task task = new Task(name, description, id, Status.NEW.getStatus(), TaskTypes.TASK);
        taskMap.put(id, task);
        id++;
        System.out.println("Задача добавлена!");
    }

    @Override
    public void createEpic(String name, String description) {
        Epic epic = new Epic(name, description, id, Status.NEW.getStatus(), TaskTypes.EPIC);
        epicMap.put(id, epic);
        epicTasks.put(epic, null);
        id++;
        System.out.println("Эпик добавлен!");
    }

    @Override
    public void createSubtask(String name, String description) {
        int epicId = 0;
        for (int o : epicMap.keySet()) {
            if (o > epicId) {
                epicId = o;
            }
        }
        Subtask sub = new Subtask(name, description, id, Status.NEW.getStatus(), TaskTypes.SUBTASK, epicId);
        subMap.put(id, sub);
        List<Subtask> subArray = new ArrayList<>();
        if (epicTasks.get((Epic) searchForTask(epicId)) != null) {
            subArray = epicTasks.get((Epic) searchForTask(epicId));
        }
        subArray.add(sub);
        epicTasks.put(epicMap.get(epicId), subArray);
        id++;
        System.out.println("Подзадача добавлена!");
    }

    @Override
    public void updateTask(int id, String status) {
        if (taskMap.containsKey(id)) {
            taskMap.get(id).setStatus(status);
        } else if (epicMap.containsKey(id)) {
            List<Subtask> subArray = epicTasks.get(epicMap.get(id));
            epicMap.get(id).setStatus(status);
            epicTasks.remove(epicMap.get(id));
            epicTasks.put(epicMap.get(id), subArray);
        } else if (subMap.containsKey(id)) {
            Subtask subToReplace = (Subtask) searchForTask(id);
            subMap.get(id).setStatus(status);
            if (searchForEpic(subToReplace) != null) {
                Epic epic1 = searchForEpic(subToReplace);
                List<Subtask> subArray2 = epicTasks.get(searchForEpic(subToReplace));
                subArray2.add(subMap.get(id));
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
    public Task searchForTask(int i) {
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

    public void addToHistory(Task task) {
        searchHistory.add(task);
    }

    @Override
    public void searchAndDelete(int i) {
        if (i >= 0 && taskMap.containsKey(i)) {
            taskMap.remove(i);
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
        } else if (i >= 0 && epicMap.containsKey(i)) {
            for (Subtask subToDel : epicTasks.get(epicMap.get(i))) {
                if (searchHistory.containsTask(subToDel.getId())) {
                    searchHistory.remove(subToDel.getId());
                }
                subMap.remove(subToDel.getId());
            }
            epicTasks.remove(epicMap.get(i));
            epicMap.remove(i);
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
        } else if (i >= 0 && subMap.containsKey(i)) {
            List<Subtask> subArray = epicTasks.get(searchForEpic((Subtask) searchForTask(i)));
            subArray.remove((Subtask) searchForTask(i));
            epicTasks.put(searchForEpic((Subtask) searchForTask(i)), subArray);
            subMap.remove(i);
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
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
    public void clearCategory(int i) {
        if (i == 1) {
            for (Task taskToClear : taskMap.values()) {
                if (searchHistory.containsTask(taskToClear.getId())) {
                    searchHistory.remove(taskToClear.getId());
                }
            }
            taskMap.clear();
        } else if (i == 2) {
            for (Epic epicToClear : epicMap.values()) {
                if (searchHistory.containsTask(epicToClear.getId())) {
                    searchHistory.remove(epicToClear.getId());
                }
            }
            for (Epic epicToHelp : epicTasks.keySet()) {
                for (Subtask subToClear : epicTasks.get(epicToHelp)) {
                    if (searchHistory.containsTask(subToClear.getId())) {
                        searchHistory.remove(subToClear.getId());
                    }
                }
            }
            epicMap.clear();
            subMap.clear();
            epicTasks.clear();
        } else if (i == 3) {
            for (Subtask subToClear : subMap.values()) {
                if (searchHistory.containsTask(subToClear.getId())) {
                    searchHistory.remove(subToClear.getId());
                }
            }
            subMap.clear();
            epicTasks.clear();
        }
    }

    public void showSearchHistory() {
        System.out.println(searchHistory.getHistory());
    }

}
