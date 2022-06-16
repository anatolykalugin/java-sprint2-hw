package com.practicum.managers;

import com.google.gson.Gson;
import com.practicum.exceptions.ManagerSaveException;
import com.practicum.kvserver.KVServer;
import com.practicum.managers.http.HttpTaskServer;
import com.practicum.tasks.Epic;
import com.practicum.tasks.Subtask;
import com.practicum.tasks.Task;
import com.practicum.tasks.TaskTypes;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class HttpTaskManagerTest {
    private static Managers managerTest;
    private static Gson gson;
    private static HttpTaskServer httpTaskServer;
    private static HttpClient httpClient;
    private static Task taskToTest;
    private static Task task2ToTest;
    private static Epic epicToTest;
    private static Subtask sub1ToTest;
    private static KVServer kvServer;

    @BeforeAll
    public static void beforeAll() {
        managerTest = new Managers();
        gson = managerTest.getGson();
        httpClient = HttpClient.newBuilder().build();
        taskToTest = new Task("Task0", "Description0", 0, "новое", TaskTypes.TASK);
        taskToTest.setDuration(Duration.ofMinutes(10));
        taskToTest.setStartTime(LocalDateTime.of(2022, 1,1,10,10));
        epicToTest = new Epic("Epic1", "Description1", 0, "новое", TaskTypes.EPIC);
        epicToTest.setDuration(Duration.ofMinutes(25));
        epicToTest.setStartTime(LocalDateTime.of(2022, 1,2,10,10));
        sub1ToTest = new Subtask("Sub2", "Description2", 1, "новое",
                TaskTypes.SUBTASK, 1);
        sub1ToTest.setDuration(Duration.ofMinutes(20));
        sub1ToTest.setStartTime(LocalDateTime.of(2022, 1,2,10,10));
        task2ToTest = new Task("Task4", "Description4", 0, "новое", TaskTypes.TASK);
        task2ToTest.setDuration(Duration.ofMinutes(10));
        task2ToTest.setStartTime(LocalDateTime.of(2022, 1,10,10,10));
    }

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка старта сервера");
        }
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void shouldCreateAndUpdateAndSearchForTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(taskToTest))).uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        URI url1 = URI.create("http://localhost:8080/tasks/?id=0");
        taskToTest.setStatus("делаю");
        HttpRequest request1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(taskToTest))).uri(url1).build();
        HttpResponse<String> response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response1.statusCode());
        URI url2 = URI.create("http://localhost:8080/tasks/?id=0");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
    }

    @Test
    public void shouldCreateEpicWithSubs() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(epicToTest))).uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(sub1ToTest))).uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
    }

    @Test
    public void shouldSearchForSubsByEpicId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(epicToTest))).uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(sub1ToTest))).uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/epic/?id=0");
        HttpRequest request3 = HttpRequest.newBuilder().GET().uri(url3).build();
        HttpResponse<String> response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
    }

    @Test
    public void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(taskToTest))).uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        URI url2 = URI.create("http://localhost:8080/tasks");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(taskToTest))).uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        URI url2 = URI.create("http://localhost:8080/tasks/?id=0");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
        URI url3 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request3 = HttpRequest.newBuilder().GET().uri(url3).build();
        HttpResponse<String> response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        URI url2 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(task2ToTest))).uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        URI url = URI.create("http://localhost:8080/tasks/?id=0");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldClearAllTasks() throws IOException, InterruptedException {
        URI url2 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers
                .ofString(gson.toJson(task2ToTest))).uri(url2).build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }
}
