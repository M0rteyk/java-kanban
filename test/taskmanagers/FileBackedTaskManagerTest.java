package taskmanagers;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;
    private static final LocalDateTime TEST_TIME = LocalDateTime.of(2025, 3, 29, 13, 46);

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            tempFile = Files.createTempFile("tasks", ".csv").toFile();
            return new FileBackedTaskManager(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл", e);
        }
    }

    @Test
    public void testSaveAndLoadFromFile() {

        Task task = new Task("Test Task", "Description", TEST_TIME, Duration.ofHours(1));
        taskManager.createTask(task);

        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Description",
                TEST_TIME, Duration.ofMinutes(30), epic.getId());
        taskManager.createSubtusk(subTask);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем задачи
        Task loadedTask = loadedManager.findTaskById(task.getId());
        assertEquals(task.getName(), loadedTask.getName());
        assertEquals(task.getStartTime(), loadedTask.getStartTime());

        // Проверяем подзадачи
        SubTask loadedSubTask = loadedManager.findSubtaskByID(subTask.getId());
        assertEquals(subTask.getEpicId(), loadedSubTask.getEpicId());
        assertEquals(subTask.getStartTime(), loadedSubTask.getStartTime());
    }
}