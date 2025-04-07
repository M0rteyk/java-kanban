package taskmanagers;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    protected LocalDateTime testTime = LocalDateTime.now();
    protected Duration testDuration = Duration.ofHours(1);

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void testEpicStatusAllNew() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                testTime, testDuration, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                testTime.plusHours(2), testDuration, epic.getId());
        taskManager.createSubtusk(subTask1);
        taskManager.createSubtusk(subTask2);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    public void testEpicStatusAllDone() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                TaskStatus.DONE, epic.getId(), testTime, testDuration);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                TaskStatus.DONE, epic.getId(), testTime.plusHours(2), testDuration);
        taskManager.createSubtusk(subTask1);
        taskManager.createSubtusk(subTask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void testEpicStatusMixed() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description",
                TaskStatus.NEW, epic.getId(), testTime, testDuration);
        SubTask subTask2 = new SubTask("SubTask 2", "Description",
                TaskStatus.DONE, epic.getId(), testTime.plusHours(2), testDuration);
        taskManager.createSubtusk(subTask1);
        taskManager.createSubtusk(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void testPrioritizedTasks() {
        LocalDateTime earlyTime = LocalDateTime.now();
        LocalDateTime lateTime = earlyTime.plusHours(1);

        Task task1 = new Task("Task 1", "Description", lateTime, Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description", earlyTime, Duration.ofMinutes(30));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size(), "Неверное количество задач");
        assertEquals(task2.getId(), prioritizedTasks.get(0).getId(), "Задачи не отсортированы по времени");
    }
}