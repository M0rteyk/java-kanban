package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.SubTask;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        handleGetAllSubtasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetSubtaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handlePostSubtask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteSubtask(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        List<SubTask> subtasks = manager.getSubTasks();
        sendSuccess(exchange, gson.toJson(subtasks));
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            SubTask subtask = manager.findSubtaskByID(id);
            if (subtask != null) {
                sendSuccess(exchange, gson.toJson(subtask));
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        Optional<String> bodyOptional = readRequestBody(exchange);
        if (bodyOptional.isEmpty()) {
            sendNotAcceptable(exchange);
            return;
        }

        String body = bodyOptional.get();
        try {
            SubTask subtask = gson.fromJson(body, SubTask.class);
            if (subtask.getId() == 0) {
                manager.createSubtusk(subtask);
                sendCreated(exchange, gson.toJson(subtask));
            } else {
                manager.updateSubtask(subtask);
                sendSuccess(exchange, gson.toJson(subtask));
            }
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            sendNotAcceptable(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            manager.clearSubtusksById(id);
            sendSuccess(exchange, "Subtask deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}