package http;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryHandlerTest extends HttpTaskServerTest {

    @Test
    public void testGetHistory() throws Exception {
        Task task = manager.createTask(new Task("Task", "Desc", TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30)));
        manager.findTaskById(task.getId()); // Добавляем в историю

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertFalse(response.body().isEmpty());
    }
}