package com.practicum.managers;

import com.practicum.exceptions.ManagerSaveException;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private String filePath;

    public FileBackedTasksManager(String filePath) {
        this.filePath = filePath;
    }

    public FileBackedTasksManager() {
    }

    private final CSVTaskSerializator serializator = new CSVTaskSerializator();

    public CSVTaskSerializator getSerializator() {
        return serializator;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public void createTask(String name, String description, long duration, String date) {
        super.createTask(name, description, duration, date);
        save();
    }

    @Override
    public void createEpic(String name, String description, long duration, String date) {
        super.createEpic(name, description, duration, date);
        save();
    }

    @Override
    public void createSubtask(String name, String description, long duration, String date) {
        super.createSubtask(name, description, duration, date);
        save();
    }

    @Override
    public void updateTask(int id, String status) {
        super.updateTask(id, status);
        save();
    }

    @Override
    public Epic searchForEpic(Subtask subtask) {
        Epic epic = super.searchForEpic(subtask);
        save();
        return epic;
    }

    @Override
    public Task searchForTask(int i) {
        return super.searchForTask(i);
    }

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }

    @Override
    public void searchAndDelete(int i) {
        super.searchAndDelete(i);
        save();
    }

    @Override
    public void clearCategory(int i) {
        super.clearCategory(i);
        save();
    }

    protected void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.append(CSVTaskSerializator.getHeader());
            bw.newLine();
            for (Task task : taskMap.values()) {
                bw.append(serializator.taskToString(task));
                bw.newLine();
            }
            for (Epic epic : epicMap.values()) {
                bw.append(serializator.taskToString(epic));
                bw.newLine();
            }
            for (Subtask sub : subMap.values()) {
                bw.append(serializator.taskToString(sub));
                bw.newLine();
            }
            bw.append(CSVTaskSerializator.getSearchHeader());
            bw.newLine();
            bw.append(CSVTaskSerializator.historyToString(searchHistory));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(String filePath) {
        FileBackedTasksManager fb1 = new FileBackedTasksManager(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(fb1.filePath))) {
            br.readLine();
            while (br.ready()) {
                String line1 = br.readLine();
                if (!line1.isBlank()) {
                    if (!line1.equals(CSVTaskSerializator.getSearchHeader())) {
                        Task task = fb1.serializator.taskFromString(line1);
                        switch (task.getType()) {
                            case TASK:
                                fb1.taskMap.put(task.getId(), task);
                                fb1.tasksTree.add(task);
                                break;
                            case EPIC:
                                fb1.epicMap.put(task.getId(), (Epic) task);
                                fb1.epicTasks.put((Epic) task, new ArrayList<>());
                                break;
                            case SUBTASK:
                                fb1.subMap.put(task.getId(), (Subtask) task);
                                fb1.epicTasks.get(fb1.epicMap.get(((Subtask) task).getEpicLinkId()))
                                        .add((Subtask) task);
                                fb1.tasksTree.add(task);
                                break;
                        }
                    } else {
                        List<Integer> newHistory = fb1.serializator.historyFromString(br.readLine());
                        for (int i : newHistory) {
                            fb1.addToHistory(fb1.searchForTask(i));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return fb1;
    }

}

