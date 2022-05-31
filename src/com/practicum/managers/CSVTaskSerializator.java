package com.practicum.managers;

import com.practicum.exceptions.UnknownTypeException;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class CSVTaskSerializator {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TaskTypes.SUBTASK)) {
            Subtask sub = (Subtask) task;
            sb.append(sub.getId()).append(",").append(sub.getType()).append(",").append(sub.getName())
                    .append(",").append(sub.getDescription()).append(",").append(sub.getStatus())
                    .append(",").append(sub.getDuration().toMinutes()).append(",")
                    .append(sub.getStartTime().format(formatter)).append(",").append(sub.getEpicLinkId());
        } else {
            sb.append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName())
                    .append(",").append(task.getDescription()).append(",").append(task.getStatus())
                    .append(",").append(task.getDuration().toMinutes()).append(",")
                    .append(task.getStartTime().format(formatter));
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
                int epicLinkId = Integer.parseInt(toTask[7]);
                Subtask sub = new Subtask(name, description, id, status, type, epicLinkId);
                sub.setDuration(Duration.ofMinutes(Integer.parseInt(toTask[5])));
                sub.setStartTime(LocalDateTime.parse(toTask[6], formatter));
                return sub;
            case EPIC:
                Epic epic = new Epic(name, description, id, status, type);
                epic.setDuration(Duration.ofMinutes(Integer.parseInt(toTask[5])));
                epic.setStartTime(LocalDateTime.parse(toTask[6], formatter));
                return epic;
            case TASK:
                Task task = new Task(name, description, id, status, type);
                task.setDuration(Duration.ofMinutes(Integer.parseInt(toTask[5])));
                task.setStartTime(LocalDateTime.parse(toTask[6], formatter));
                return task;
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
        List<Integer> recreatedHistory = new ArrayList<>();
        if (value != null) {
            String[] toHistory = value.split(",");
            for (String s : toHistory) {
                recreatedHistory.add(Integer.parseInt(s));
            }
            return recreatedHistory;
        }
        return recreatedHistory;
    }

    public static String getHeader() {
        return "id,type,name,status,description,duration,startTime,epic";
    }

    public static String getSearchHeader() {
        return "search history:";
    }

}
