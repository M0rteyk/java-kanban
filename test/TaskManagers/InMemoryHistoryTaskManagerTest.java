package TaskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.TaskStatus;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    public void getHistoryShouldReturnListOf10Tasks() {
        for (int i = 0; i < 20; i++) {
            taskManager.createTask(new Task("Some name", "Some description"));
        }

        List<Task> tasks = taskManager.getAllTasks();
        for (Task task : tasks) {
            taskManager.findTaskById(task.getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(10, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task1 = new Task("task1", "task1 description");
        taskManager.createTask(task1);
        taskManager.findTaskById(task1.getId());
        task1.setName("task2");
        task1.setDescription("task2 description");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertNotEquals(task1.getName(), oldTask.getName(), "В истории не сохранилась старая версия задачи");
        assertNotEquals(task1.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        taskManager.findEpicById(epic1.getId());
        epic1.setName("epic2");
        epic1.setDescription("epic2 description");
        epic1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        List<Task> epics = taskManager.getHistory();
        Task oldEpic = epics.getFirst();
        assertNotEquals(epic1.getName(), oldEpic.getName(),
                "В истории не сохранилась старая версия эпика");
        assertNotEquals(epic1.getDescription(), oldEpic.getDescription(),
                "В истории не сохранилась старая версия эпика");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        SubTask subtask3 = new SubTask("subtask1", "subtask1 description",
                epic1.getId());
        taskManager.createSubtusk(subtask3);
        taskManager.findSubtaskByID(subtask3.getId());
        subtask3.setName("subtask2");
        subtask3.setDescription("subtask2 description");
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);
        List<Task> subtasks = taskManager.getHistory();
        Task oldSubtask = subtasks.getFirst();
        assertNotEquals(subtask3.getName(), oldSubtask.getName(),
                "В истории не сохранилась старая версия подзадачи");
        assertNotEquals(subtask3.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась старая версия подзадачи");
    }
}