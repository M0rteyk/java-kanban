package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager manager, Gson gson) {
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
                        handleGetAllTasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetTaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handlePostTask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteTask(exchange, pathParts[2]);
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

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = manager.getAllTasks();
        sendSuccess(exchange, gson.toJson(tasks));
    }

    private void handleGetTaskById(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            Task task = manager.findTaskById(id);
            if (task != null) {
                sendSuccess(exchange, gson.toJson(task));
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        Optional<String> bodyOptional = readRequestBody(exchange);
        if (bodyOptional.isEmpty()) {
            sendNotAcceptable(exchange);
            return;
        }

        String body = bodyOptional.get();
        try {
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() == 0) {
                manager.createTask(task);
                sendCreated(exchange, gson.toJson(task));
            } else {
                manager.updateTask(task);
                sendSuccess(exchange, gson.toJson(task));
            }
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            sendNotAcceptable(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleDeleteTask(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            manager.deleteTaskById(id);
            sendSuccess(exchange, "Task deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}