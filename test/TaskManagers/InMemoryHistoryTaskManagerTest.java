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
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task1 = new Task("task1", "task1 description");
        taskManager.createTask(task1);
        taskManager.findTaskById(task1.getId());
        Task oldTask = new Task(task1.getName(), task1.getDescription());
        task1.setName("task2");
        task1.setDescription("task2 description");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.findTaskById(task1.getId());
        List<Task> tasks = taskManager.getHistory();
        Task historyTask = tasks.getFirst();
        assertNotEquals(oldTask.getName(), historyTask.getName(), "В истории не сохранилась старая версия задачи");
        assertNotEquals(oldTask.getDescription(), historyTask.getDescription(),
                "В истории не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        taskManager.findEpicById(epic1.getId());
        Epic oldEpic = new Epic(epic1.getName(), epic1.getDescription());
        epic1.setName("epic2");
        epic1.setDescription("epic2 description");
        epic1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        taskManager.findEpicById(epic1.getId());
        List<Task> epics = taskManager.getHistory();
        Task historyEpic = epics.getFirst();
        assertNotEquals(oldEpic.getName(), historyEpic.getName(),
                "В истории не сохранилась старая версия эпика");
        assertNotEquals(oldEpic.getDescription(), historyEpic.getDescription(),
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
        Task oldSubtask = new SubTask(subtask3.getName(), subtask3.getDescription(), subtask3.getEpicId());
        subtask3.setName("subtask2");
        subtask3.setDescription("subtask2 description");
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);
        taskManager.findSubtaskByID(subtask3.getId());
        List<Task> subtasks = taskManager.getHistory();
        Task historySubtask = subtasks.getFirst();
        assertNotEquals(historySubtask.getName(), oldSubtask.getName(),
                "В истории не сохранилась старая версия подзадачи");
        assertNotEquals(historySubtask.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась старая версия подзадачи");
    }
}