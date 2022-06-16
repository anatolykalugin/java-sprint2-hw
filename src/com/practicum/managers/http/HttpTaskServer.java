package com.practicum.managers.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.practicum.managers.Managers;
import com.practicum.managers.TaskManager;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    Managers manager = new Managers();

    public HttpTaskServer() throws IOException, InterruptedException {
        taskManager = manager.getDefault();
        gson = manager.getGson();
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    private void handler(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String query = h.getRequestURI().getQuery();
        if (path.contains("/tasks")) {
            System.out.println("Началась обработка /tasks от клиента.");
            String method = h.getRequestMethod();
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks/") || path.endsWith("/tasks")) {
                        if (query == null) {
                            System.out.println("Началась обработка GET /tasks/ от клиента.");
                            h.sendResponseHeaders(200, 0);
                            String response = "Список задач по приоритету: " + gson.toJson(taskManager
                                    .getPrioritizedTasks());
                            try (OutputStream os = h.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            System.out.println("Началась обработка GET /tasks/?id=x от клиента.");
                            String[] querySplit = query.split("=");
                            int neededId = Integer.parseInt(querySplit[1]);
                            if (taskManager.searchForTask(neededId) != null) {
                                taskManager.addToHistory(taskManager.searchForTask(neededId));
                                h.sendResponseHeaders(200, 0);
                                String response = "Нашли такую задачу: " + gson.toJson(taskManager
                                        .searchForTask(neededId));
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                h.sendResponseHeaders(404, 0);
                                String response = "Нет такой задачи";
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                    } else if (path.endsWith("/history") || path.endsWith("/history/")) {
                        System.out.println("Началась обработка GET /tasks/history от клиента.");
                        h.sendResponseHeaders(200, 0);
                        String response = "Список истории задач: " + gson.toJson(taskManager
                                .showSearchHistory());
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if ((path.endsWith("/subtask/epic/")) || (path.endsWith("/subtask/epic"))
                            && (query != null)) {
                        System.out.println("Началась обработка GET /tasks/subtask/epic/?id=x от клиента.");
                        String[] querySplit = query.split("=");
                        int neededId = Integer.parseInt(querySplit[1]);
                        if ((taskManager.searchForTask(neededId) != null)
                                && (taskManager.searchForTask(neededId).getType().equals(TaskTypes.EPIC))) {
                            h.sendResponseHeaders(200, 0);
                            String response = "Список подзадач указанного эпика: " + gson.toJson(taskManager
                                    .showSubs(neededId));
                            try (OutputStream os = h.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            h.sendResponseHeaders(404, 0);
                            String response = "Нет такого эпика";
                            try (OutputStream os = h.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    } else if (path.endsWith("/task") || path.endsWith("/task/")) {
                        System.out.println("Началась обработка GET /tasks/task от клиента.");
                        h.sendResponseHeaders(200, 0);
                        String response = "Список задач: " + gson.toJson(taskManager.showAllTasks());
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (path.endsWith("/tasks/epic") || path.endsWith("/tasks/epic/")) {
                        System.out.println("Началась обработка GET /tasks/epic от клиента.");
                        h.sendResponseHeaders(200, 0);
                        String response = "Список эпиков: " + gson.toJson(taskManager.showAllEpics());
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (path.endsWith("/subtask") || path.endsWith("/subtask/")) {
                        System.out.println("Началась обработка GET /tasks/subtask от клиента.");
                        h.sendResponseHeaders(200, 0);
                        String response = "Список подзадач: " + gson.toJson(taskManager.showAllSubs());
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(400, 0);
                        String response = "Некорректный запрос";
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                case "POST":
                    InputStream is = h.getRequestBody();
                    if (is != null) {
                        String body = new String(is.readAllBytes(), CHARSET);
                        if (query == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            if (path.endsWith("/task") || path.endsWith("/task/")) {
                                System.out.println("Началась обработка POST /tasks/task от клиента.");
                                Task task1 = gson.fromJson(body, Task.class);
                                taskManager.createTask(task1.getName(), task1.getDescription(),
                                        task1.getDuration().toMinutes(), task1.getStartTime().format(formatter));
                                h.sendResponseHeaders(201, 0);
                                String response = "Действие выполнено";
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else if (path.endsWith("/epic") || path.endsWith("/epic/")) {
                                System.out.println("Началась обработка POST /tasks/epic от клиента.");
                                Epic epic1 = gson.fromJson(body, Epic.class);
                                taskManager.createEpic(epic1.getName(), epic1.getDescription(),
                                        epic1.getDuration().toMinutes(), epic1.getStartTime().format(formatter));
                                String response = "Действие выполнено";
                                h.sendResponseHeaders(201, 0);
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else if (path.endsWith("/subtask") || path.endsWith("/subtask/")) {
                                System.out.println("Началась обработка POST /tasks/subtask от клиента.");
                                Subtask sub1 = gson.fromJson(body, Subtask.class);
                                taskManager.createSubtask(sub1.getName(), sub1.getDescription(),
                                        sub1.getDuration().toMinutes(), sub1.getStartTime().format(formatter));
                                String response = "Действие выполнено";
                                taskManager.addEpicStartTime(sub1.getStartTime().format(formatter));
                                taskManager.addEpicDuration(sub1.getDuration().toMinutes());
                                taskManager.addEpicEndTime();
                                h.sendResponseHeaders(201, 0);
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                h.sendResponseHeaders(400, 0);
                                String response = "Некорректный запрос";
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        } else if (path.endsWith("/tasks/") || path.endsWith("/tasks")) {
                            System.out.println("Началась обработка POST /tasks/?id=x от клиента.");
                            String[] querySplit = query.split("=");
                            JsonElement je = JsonParser.parseString(body);
                            if (je.isJsonObject()) {
                                JsonObject jo = je.getAsJsonObject();
                                int neededId = Integer.parseInt(querySplit[1]);
                                if (taskManager.searchForTask(neededId) != null) {
                                    taskManager.updateTask(neededId, jo.get("status").getAsString());
                                    String response = "Задача успешно обновлена";
                                    h.sendResponseHeaders(201, 0);
                                    try (OutputStream os = h.getResponseBody()) {
                                        os.write(response.getBytes());
                                    }
                                } else {
                                    String response = "Нет задачи для редактирования";
                                    h.sendResponseHeaders(404, 0);
                                    try (OutputStream os = h.getResponseBody()) {
                                        os.write(response.getBytes());
                                    }
                                }
                            }
                        } else {
                            String response = "Несуществующий запрос на добавление/редактирование.";
                            h.sendResponseHeaders(400, 0);
                            try (OutputStream os = h.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    } else {
                        String response = "Не заполнено тело запроса";
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                case "DELETE":
                    if (path.endsWith("/tasks/") || path.endsWith("/tasks")) {
                        if (query != null) {
                            System.out.println("Началась обработка DELETE /tasks/?id=x от клиента.");
                            String[] querySplit = query.split("=");
                            int neededId = Integer.parseInt(querySplit[1]);
                            if (taskManager.searchForTask(neededId) != null) {
                                h.sendResponseHeaders(200, 0);
                                taskManager.searchAndDelete(neededId);
                                String response = "Удалили такую задачу";
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                h.sendResponseHeaders(404, 0);
                                String response = "Нет такой задачи";
                                try (OutputStream os = h.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        } else {
                            h.sendResponseHeaders(400, 0);
                            String response = "Неправильный запрос на удаление задачи";
                            try (OutputStream os = h.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    } else if (path.endsWith("/task") || path.endsWith("/task/")) {
                        h.sendResponseHeaders(200, 0);
                        taskManager.clearCategory(1);
                        String response = "Удалили все задачи";
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (path.endsWith("/epic") || path.endsWith("/epic/")) {
                        h.sendResponseHeaders(200, 0);
                        taskManager.clearCategory(2);
                        String response = "Удалили все эпики и подзадачи";
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (path.endsWith("/subtask") || path.endsWith("/subtask/")) {
                        h.sendResponseHeaders(200, 0);
                        taskManager.clearCategory(3);
                        String response = "Удалили все подзадачи";
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(400, 0);
                        String response = "Некорректный запрос";
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                default:
                    String response = "Неверный метод";
                    h.sendResponseHeaders(400, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
            }
        } else {
            String response = "Неверный путь";
            h.sendResponseHeaders(400, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public void start() {
        System.out.println("Сервер запущен на " + PORT + " порту!");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

}