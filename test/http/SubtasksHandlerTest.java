package http;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.TaskStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtasksHandlerTest extends HttpTaskServerTest {

    @Test
    public void testCreateSubtask() throws Exception {
        // Сначала создаем эпик
        var epic = manager.createEpic(new Epic("Epic", "Epic desc"));

        SubTask subtask = new SubTask("Subtask", "Desc", TaskStatus.NEW, epic.getId(), LocalDateTime.now(),
                Duration.ofMinutes(30));
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getSubTasks().size());
    }
}