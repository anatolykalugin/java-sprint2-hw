package com.practicum.tasks;

public class Subtask extends Task {

    public Subtask(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public static Subtask createSubtask(int id, String status) {
//        Scanner subScanner = new Scanner(System.in);
        System.out.println("Введите название подзадачи");
//        String name = subScanner.next();
        String name = "Подзадача";
        System.out.println("Введите описание подзадачи");
//        String description = subScanner.next();
        String description = "описание подзадачи";

        Subtask subtask = new Subtask(name, description, id, status);

        System.out.println("Подзадача добавлена!");
        return subtask;
    }

}
