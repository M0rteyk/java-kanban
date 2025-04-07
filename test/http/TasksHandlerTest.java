package http;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest extends HttpTaskServerTest {

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Test", "Description", TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = manager.createTask(new Task("Test", "Desc", TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/" + task.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getId(), receivedTask.getId());
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        manager.createTask(new Task("Task1", "Desc1", TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30)));
        manager.createTask(new Task("Task2", "Desc2", TaskStatus.IN_PROGRESS, LocalDateTime.now(),
                Duration.ofMinutes(10)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length);
    }
}