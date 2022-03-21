package com.practicum.tasks;

import java.util.Scanner;

public class Epic extends Task {

    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public static Object createEpic(int id, String status) {
        Scanner epicScanner = new Scanner(System.in);
        System.out.println("Введите название эпика");
        String name = epicScanner.next();
        System.out.println("Введите описание эпика");
        String description = epicScanner.next();

        Epic epic = new Epic(name, description, id, status);

        System.out.println("Эпик добавлен!");
        return epic;
    }

}