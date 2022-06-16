package com.practicum.managers;

import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public HistoryManager searchHistory = new Managers().getDefaultHistory();

    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();
    public HashMap<Integer, Subtask> subMap = new HashMap<>();
    public HashMap<Epic, List<Subtask>> epicTasks = new HashMap<>();
    public TreeSet<Task> tasksTree = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    int id = 0;

    @Override
    public void createTask(String name, String description, long duration, String date) {
        Task task = new Task(name, description, id, Status.NEW.getStatus(), TaskTypes.TASK);
        addDurationAndDate(task, duration, date);
        if (timeIsValid(task)) {
            addToTree(task);
            taskMap.put(id, task);
            id++;
            System.out.println("Задача добавлена!");
        } else {
            System.out.println("Срок выполнения задачи пересекается с другими.");
            System.out.println("Попробуйте заново.");
        }
    }

    @Override
    public void createEpic(String name, String description, long duration, String date) {
        Epic epic = new Epic(name, description, id, Status.NEW.getStatus(), TaskTypes.EPIC);
        epic.setDuration(Duration.ofMinutes(duration));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        epic.setStartTime(LocalDateTime.parse(date, formatter));
        epicMap.put(id, epic);
        epicTasks.put(epic, null);
        id++;
        System.out.println("Эпик добавлен!");
    }

    @Override
    public void createSubtask(String name, String description, long duration, String date) {
        int epicId = 0;
        for (int o : epicMap.keySet()) {
            if (o > epicId) {
                epicId = o;
            }
        }
        Subtask sub = new Subtask(name, description, id, Status.NEW.getStatus(), TaskTypes.SUBTASK, epicId);
        addDurationAndDate(sub, duration, date);
        if (timeIsValid(sub)) {
            addToTree(sub);
            subMap.put(id, sub);
            List<Subtask> subArray = new ArrayList<>();
            if (epicTasks.get((Epic) searchForTask(epicId)) != null) {
                subArray = epicTasks.get((Epic) searchForTask(epicId));
            }
            subArray.add(sub);
            epicMap.put(epicId, epicMap.get(epicId));
            epicTasks.put(epicMap.get(epicId), subArray);
            id++;
            System.out.println("Подзадача добавлена!");
        } else {
            System.out.println("Срок выполнения подзадачи пересекается с другими.");
            System.out.println("Попробуйте заново.");
        }
    }

    @Override
    public void updateTask(int id, String status) {
        if (taskMap.containsKey(id)) {
            tasksTree.remove(taskMap.get(id));
            taskMap.get(id).setStatus(status);
            addToTree(taskMap.get(id));
        } else if (epicMap.containsKey(id)) {
            List<Subtask> subArray = epicTasks.get(epicMap.get(id));
            epicMap.get(id).setStatus(status);
            epicTasks.remove(epicMap.get(id));
            epicTasks.put(epicMap.get(id), subArray);
        } else if (subMap.containsKey(id)) {
            Subtask subToReplace = (Subtask) searchForTask(id);
            tasksTree.remove(subMap.get(id));
            subMap.get(id).setStatus(status);
            addToTree(subMap.get(id));
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
    public List<Task> showAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> showAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> showAllSubs() {
        return new ArrayList<>(subMap.values());
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
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
            tasksTree.remove(taskMap.get(i));
            taskMap.remove(i);
        } else if (i >= 0 && epicMap.containsKey(i)) {
            for (Subtask subToDel : epicTasks.get(epicMap.get(i))) {
                if (searchHistory.containsTask(subToDel.getId())) {
                    searchHistory.remove(subToDel.getId());
                }
                tasksTree.remove(subMap.get(i));
                subMap.remove(subToDel.getId());
            }
            epicTasks.remove(epicMap.get(i));
            tasksTree.remove(epicMap.get(i));
            epicMap.remove(i);
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
        } else if (i >= 0 && subMap.containsKey(i)) {
            List<Subtask> subArray = epicTasks.get(searchForEpic((Subtask) searchForTask(i)));
            subArray.remove((Subtask) searchForTask(i));
            epicTasks.put(searchForEpic((Subtask) searchForTask(i)), subArray);
            tasksTree.remove(subMap.get(i));
            subMap.remove(i);
            if (searchHistory.containsTask(i)) {
                searchHistory.remove(i);
            }
        } else {
            System.out.println("Нет задачи с таким идентификатором");
        }
    }

    @Override
    public List<Subtask> showSubs(int i) {
        if (epicTasks.containsKey(searchForTask(i))) {
            return epicTasks.get((Epic) searchForTask(i));
        } else {
            return List.of();
        }
    }

    @Override
    public void clearCategory(int i) {
        if (i == 1) {
            for (Task taskToClear : taskMap.values()) {
                tasksTree.remove(taskToClear);
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
                    tasksTree.remove(subToClear);
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
                tasksTree.remove(subToClear);
                if (searchHistory.containsTask(subToClear.getId())) {
                    searchHistory.remove(subToClear.getId());
                }
            }
            subMap.clear();
            epicTasks.clear();
        }
    }

    public List<Task> showSearchHistory() {
        return searchHistory.getHistory();
    }

    @Override
    public void addDurationAndDate(Task task, long duration, String date) {
        task.setDuration(Duration.ofMinutes(duration));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        task.setStartTime(LocalDateTime.parse(date, formatter));
    }

    @Override
    public void addEpicDuration(long duration) {
        searchForTask(((Subtask) searchForTask(id - 1)).getEpicLinkId()).addDuration(duration);
    }

    public void addEpicStartTime(String startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime currentEpicDate = searchForTask(((Subtask) searchForTask(id - 1))
                .getEpicLinkId()).getStartTime();
        if (currentEpicDate.isEqual(LocalDateTime.of(1970,1,1,0,0))) {
            searchForTask(((Subtask) searchForTask(id - 1)).getEpicLinkId()).
                    setStartTime(LocalDateTime.parse(startDate, formatter));
        } else if (currentEpicDate.isAfter(LocalDateTime.parse(startDate, formatter))) {
            searchForTask(((Subtask) searchForTask(id - 1)).getEpicLinkId()).
                    setStartTime(LocalDateTime.parse(startDate, formatter));
        }
    }

    public void addEpicEndTime() {
        LocalDateTime endDate = searchForTask(id - 1).getEndTime();
        LocalDateTime currentEpicDate = ((Epic) searchForTask(((Subtask) searchForTask(id - 1))
                .getEpicLinkId())).getEndDate();
        if (currentEpicDate.isEqual(LocalDateTime.of(1970,1,1,0,1))) {
            ((Epic) searchForTask(((Subtask) searchForTask(id - 1)).getEpicLinkId())).setEndDate(endDate);
        } else if (currentEpicDate.isBefore(endDate)) {
            ((Epic) searchForTask(((Subtask) searchForTask(id - 1)).getEpicLinkId())).setEndDate(endDate);
        }
    }

    public void addToTree(Task task) {
        tasksTree.add(task);
    }

    public List<Task> getPrioritizedTasks() {
        System.out.println("Список задач по приоритету: ");
        return new ArrayList<>(tasksTree);
    }

    public boolean timeIsValid(Task task) {
        boolean isValid = true;
        if (!tasksTree.isEmpty()) {
            for (Task taskToCheck : tasksTree) {
                if ((task.getStartTime().isEqual(taskToCheck.getStartTime())) ||
                        (task.getEndTime().isEqual(taskToCheck.getEndTime())) ||
                        ((task.getStartTime().isBefore(taskToCheck.getEndTime())) &&
                                (task.getStartTime().isAfter(taskToCheck.getStartTime()))) ||
                        ((task.getEndTime().isBefore(taskToCheck.getEndTime())) &&
                                (task.getEndTime().isAfter(taskToCheck.getStartTime()))) ||
                        ((task.getStartTime().isBefore(taskToCheck.getStartTime())) &&
                                (task.getEndTime().isAfter(taskToCheck.getEndTime()))) ||
                        ((task.getStartTime().isAfter(taskToCheck.getStartTime())) &&
                                (task.getEndTime().isBefore(taskToCheck.getEndTime())))) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

}
