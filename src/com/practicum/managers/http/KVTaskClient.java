package com.practicum.managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    public HttpClient client;
    private String token;
    private final URI url;

    public KVTaskClient(String url) {
        this.client = HttpClient.newHttpClient();
        this.url = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                this.token = response.body();
            } else {
                System.out.println("Ошибка при регистрации - код " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Ошибка при регистрации - исключение");
        }
    }

    public void put(String key, String json) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/save/%s/?API_TOKEN=%s", url, key, token)))
                .POST(body)
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("put успешно выполнен");
            } else {
                System.out.println("Ошибка в put - код " + response.statusCode());
            }
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Ошибка в put - исключение");
        }
    }

    public String load(String key) {
        String managerStatusToLoad = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("%s/load/%s/?API_TOKEN=%s", url, key, token)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                managerStatusToLoad = response.body();
            } else {
                System.out.println("Ошибка в методе load - код " + response.statusCode());
            }
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Ошибка в методе load - исключение");
        }
        return managerStatusToLoad;
    }
}