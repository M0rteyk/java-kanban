package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Epic;
import task.SubTask;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager, Gson gson) {
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
                        handleGetAllEpics(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetEpicById(exchange, pathParts[2]);
                    } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                        handleGetEpicSubtasks(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handlePostEpic(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteEpic(exchange, pathParts[2]);
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

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getAllEpics();
        sendSuccess(exchange, gson.toJson(epics));
    }

    private void handleGetEpicById(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            Epic epic = manager.findEpicById(id);
            if (epic != null) {
                sendSuccess(exchange, gson.toJson(epic));
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, String epicIdString) throws IOException {
        try {
            int epicId = Integer.parseInt(epicIdString);
            Epic epic = manager.findEpicById(epicId);

            if (epic == null) {
                sendNotFound(exchange);
                return;
            }

            List<SubTask> subtasks = manager.getEpicSubtasks(epic);
            sendSuccess(exchange, gson.toJson(subtasks));
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        Optional<String> bodyOptional = readRequestBody(exchange);
        if (bodyOptional.isEmpty()) {
            sendNotAcceptable(exchange);
            return;
        }

        String body = bodyOptional.get();
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic.getId() == 0) {
                manager.createEpic(epic);
                sendCreated(exchange, gson.toJson(epic));
            } else {
                manager.updateEpic(epic);
                sendSuccess(exchange, gson.toJson(epic));
            }
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            sendNotAcceptable(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange, String idString) throws IOException {
        try {
            int id = Integer.parseInt(idString);
            manager.deleteEpicById(id);
            sendSuccess(exchange, "Epic deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}