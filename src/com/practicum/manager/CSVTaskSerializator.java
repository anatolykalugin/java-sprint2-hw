package com.practicum.manager;

import com.practicum.exceptions.UnknownTypeException;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;

import java.util.ArrayList;
import java.util.List;

class CSVTaskSerializator {

    public String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TaskTypes.SUBTASK)) {
            Subtask sub = (Subtask) task;
            sb.append(sub.getId()).append(",").append(sub.getType()).append(",").append(sub.getName())
                    .append(",").append(sub.getDescription()).append(",").append(sub.getStatus())
                    .append(",").append(sub.getEpicLinkId());
        } else {
            sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName())
                    .append(",").append(task.getDescription()).append(",").append(task.getStatus());
        }
        return sb.toString();
    }

    public Task taskFromString(String value) {
        String[] toTask = value.split(",");
        int id = Integer.parseInt(toTask[0]);
        TaskTypes type = TaskTypes.valueOf(toTask[1]);
        String name = toTask[2];
        String description = toTask[3];
        String status = toTask[4];
        switch (type) {
            case SUBTASK:
                int epicLinkId = Integer.parseInt(toTask[5]);
                return new Subtask(name, description, id, status, type, epicLinkId);
            case EPIC:
                return new Epic(name, description, id, status, type);
            case TASK:
                return new Task(name, description, id, status, type);
            default:
                throw new UnknownTypeException("Неизвстный тип задачи - " + type);
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder sb2 = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            for (int i = 0; i < manager.getHistory().size(); i++) {
                Task task = manager.getHistory().get(i);
                if (i != (manager.getHistory().size() - 1)) {
                    sb2.append(task.getId()).append(",");
                } else {
                    sb2.append(task.getId());
                }
            }
        }
        return sb2.toString();
    }

    public List<Integer> historyFromString(String value) {
        String[] toHistory = value.split(",");
        List<Integer> recreatedHistory = new ArrayList<>();
        for (String s : toHistory) {
            recreatedHistory.add(Integer.parseInt(s));
        }
        return recreatedHistory;
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String getSearchHeader() {
        return "search history:";
    }

}
