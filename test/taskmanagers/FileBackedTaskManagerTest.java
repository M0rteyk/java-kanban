package taskmanagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("testFile.csv");
        if (file.exists()) {
            file.delete();
        }
        taskManager = new FileBackedTaskManager(file);
    }


    @Test
    public void testSaveAndLoadFromFile() {
        Task task = new Task("Test Task", "Test description", TaskStatus.NEW);
        taskManager.createTask(task);


        taskManager.saveFile();


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        Task loadedTask = loadedManager.findTaskById(task.getId());

        assertNotNull(loadedTask, "Задача должна быть загружена из файла");
        assertEquals(task.getName(), loadedTask.getName(), "Имя задачи должно совпадать");
        assertEquals(task.getDescription(), loadedTask.getDescription(), "Описание задачи должно совпадать");
        assertEquals(task.getStatus(), loadedTask.getStatus(), "Статус задачи должен совпадать");
    }

    @Test
    public void testHistorySaving() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.addToHistory(task1.getId());
        taskManager.addToHistory(task2.getId());


        taskManager.saveFile();


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);


        assertTrue(loadedManager.getHistoryManager().getHistory().contains(task1), "История должна содержать Task 1");
        assertTrue(loadedManager.getHistoryManager().getHistory().contains(task2), "История должна содержать Task 2");
    }

}
