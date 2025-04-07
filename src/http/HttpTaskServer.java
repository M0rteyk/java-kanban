package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import taskmanagers.Managers;
import taskmanagers.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson;
    private final File dataFile;

    public HttpTaskServer() throws IOException {
        // Инициализация файла данных
        this.dataFile = new File("src/resourses/data.csv");
        System.out.println("Путь к файлу данных: " + dataFile.getAbsolutePath());

        // Обработка, на случай отсутствия файла
        if (!dataFile.exists()) {
            System.out.println("Файл не найден, создаем новый...");
            boolean created = dataFile.createNewFile();
            if (!created) {
                throw new IOException("Не удалось создать файл данных");
            }
        }

        // Инициализация менеджера
        this.taskManager = Managers.getDefaultTaskManager(dataFile);
        this.gson = Managers.getGson();

        // Создание сервера
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Регистрация обработчиков
        registerHandlers();
    }

    private void registerHandlers() {
        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public void start() {
        server.start();
        System.out.println("HTTPTaskServer запущен на порту " + PORT);
        System.out.println("Файл данных: " + dataFile.getAbsolutePath());
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }
}