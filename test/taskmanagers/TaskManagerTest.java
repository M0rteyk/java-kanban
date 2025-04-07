package taskmanagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected LocalDateTime testTime = LocalDateTime.now();
    protected Duration testDuration = Duration.ofHours(1);

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Description", testTime, testDuration);
        taskManager.createTask(task);

        Task savedTask = taskManager.findTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
        assertEquals(testTime, savedTask.getStartTime(), "Время начала не совпадает");
        assertEquals(testDuration, savedTask.getDuration(), "Длительность не совпадает");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Test Task", "Description", testTime, testDuration);
        taskManager.createTask(task);

        Task updatedTask = new Task("Updated Task", "Updated Description",
                TaskStatus.IN_PROGRESS, testTime.plusHours(1), testDuration.plusHours(1));
        updatedTask.setId(task.getId());
        taskManager.updateTask(updatedTask);

        Task savedTask = taskManager.findTaskById(task.getId());
        assertEquals("Updated Task", savedTask.getName());
        assertEquals("Updated Description", savedTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, savedTask.getStatus());
        assertEquals(testTime.plusHours(1), savedTask.getStartTime());
        assertEquals(testDuration.plusHours(1), savedTask.getDuration());
    }

    @Test
    public void testCreateAndGetEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        Epic savedEpic = taskManager.findEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    public void testCreateAndGetSubTask() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test SubTask", "Description",
                testTime, testDuration, epic.getId());
        taskManager.createSubtusk(subTask);

        SubTask savedSubTask = taskManager.findSubtaskByID(subTask.getId());
        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        assertEquals(epic.getId(), savedSubTask.getEpicId(), "ID эпика не совпадает");
        assertEquals(testTime, savedSubTask.getStartTime(), "Время начала не совпадает");
        assertEquals(testDuration, savedSubTask.getDuration(), "Длительность не совпадает");
    }

    @Test
    public void testEpicTimeCalculation() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                testTime, testDuration, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                testTime.plusHours(2), testDuration, epic.getId());

        taskManager.createSubtusk(subTask1);
        taskManager.createSubtusk(subTask2);

        Epic savedEpic = taskManager.findEpicById(epic.getId());
        assertEquals(testTime, savedEpic.getStartTime(), "Неверное время начала эпика");
        assertEquals(testTime.plusHours(3), savedEpic.getEndTime(), "Неверное время окончания эпика");
        assertEquals(Duration.ofHours(2), savedEpic.getDuration(), "Неверная продолжительность эпика");
    }
}