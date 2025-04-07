package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.Managers;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;

public abstract class HttpTaskServerTest {
    protected static final int PORT = 8080;
    protected TaskManager manager;
    protected HttpServer server;
    protected Gson gson;
    protected HttpClient client;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Регистрация обработчиков
        server.createContext("/tasks", new TaskHandler(manager, gson));
        server.createContext("/subtasks", new SubtaskHandler(manager, gson));
        server.createContext("/epics", new EpicHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(manager, gson));

        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }
}