
package TaskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.TaskStatus;
import task.Task;
import task.SubTask;
import task.Epic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void addNewTask() {

        final Task task = taskManager.createTask(new Task("Test addNewTask", "Test addNewTask description"));
        final Task savedTask = taskManager.findTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicAndSubtasks() {
        //проверяем, что InMemoryTaskManager добавляет эпики и подзадачи и может найти их по id;
        final Epic epic1 = taskManager.createEpic(new Epic("epic1",
                "epic1 description"));
        final SubTask subtask1 = taskManager.createSubtusk(new SubTask("subtask1",
                "subtask1 description", epic1.getId()));
        final SubTask subtask2 = taskManager.createSubtusk(new SubTask("subtask2",
                "subtask2 description", epic1.getId()));
        final SubTask subtask3 = taskManager.createSubtusk(new SubTask("subtask3", "subtask3 description",
                epic1.getId()));
        final Epic savedEpic = taskManager.findEpicById(epic1.getId());
        final SubTask savedSubtask1 = taskManager.findSubtaskByID(subtask1.getId());
        final SubTask savedSubtask2 = taskManager.findSubtaskByID(subtask2.getId());
        final SubTask savedSubtask3 = taskManager.findSubtaskByID(subtask3.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");
        assertEquals(subtask3, savedSubtask3, "Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.getFirst(), "Эпики не совпадают.");

        final List<SubTask> subtasks = taskManager.getSubTasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    public void updateTaskShouldReturnTaskWithTheSameId() {
        final Task task1 = new Task("task1", "task1 description");
        taskManager.createTask(task1);
        task1.setName("task2");
        task1.setDescription("task2 description");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        final Task task2 = taskManager.updateTask(task1);
        assertEquals(task1.getId(), task2.getId(), "Вернулась задачи с другим id");
    }

    @Test
    public void updateEpicShouldReturnEpicWithTheSameId() {
        final Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        epic1.setName("epic2");
        epic1.setDescription("epic2 description");
        epic1.setStatus(TaskStatus.IN_PROGRESS);
        final Epic epic2 = taskManager.updateEpic(epic1);
        final Epic epic3 = taskManager.updateEpic(epic2);
        assertEquals(epic1.getId(), epic3.getId(), "Вернулся эпик с другим id");
    }

    @Test
    public void updateSubtaskShouldReturnSubtaskWithTheSameId() {
        final Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        final SubTask subtask1 = taskManager.createSubtusk(new SubTask("subtask1",
                "subtask1 description", epic1.getId()));
        subtask1.setName("subtask2");
        subtask1.setDescription("subtask2 description");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        final SubTask subtask2 = taskManager.updateSubtask(subtask1);
        final SubTask subtask3 = taskManager.updateSubtask(subtask2);
        assertEquals(subtask1.getId(), subtask3.getId(), "Вернулась подзадача с другим id");
    }

    @Test
    public void deleteTasksShouldReturnEmptyList() {
        taskManager.createTask(new Task("task1", "task1 description"));
        taskManager.createTask(new Task("task2", "task2 description"));
        taskManager.deleteAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        assertTrue(tasks.isEmpty(), "После удаления задач список должен быть пуст.");
    }

    @Test
    public void deleteEpicsShouldReturnEmptyList() {
        taskManager.createEpic(new Epic("epic1", "epic1 description"));
        taskManager.deleteAllEpic();
        List<Epic> epics = taskManager.getAllEpics();
        assertTrue(epics.isEmpty(), "После удаления эпиков список должен быть пуст.");
    }

    @Test
    public void deleteSubtasksShouldReturnEmptyList() {
        Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        taskManager.createSubtusk(new SubTask("subtask1",
                "subtask1 description", epic1.getId()));
        taskManager.createSubtusk(new SubTask("subtask2",
                "subtask2 description", epic1.getId()));
        taskManager.createSubtusk(new SubTask("subtask3",
                "subtask3 description", epic1.getId()));

        taskManager.clearSubtusks();
        List<SubTask> subtasks = taskManager.getSubTasks();
        assertTrue(subtasks.isEmpty(), "После удаления подзадач список должен быть пуст.");
    }

    @Test
    public void deleteTaskByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.createTask(new Task("task1", "task1 description"));
        taskManager.createTask(new Task("task2", "task2 description"));
        assertNull(taskManager.deleteTaskById(2));
    }

    @Test
    public void deleteEpicByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.createEpic(new Epic("epic1", "epic1 description"));
        assertNull(taskManager.deleteEpicById(1));
    }

    @Test
    public void deleteSubtaskByIdShouldReturnNullIfKeyIsMissing() {
        Epic epic1 = new Epic("epic1", "epic1 description");
        taskManager.createEpic(epic1);
        taskManager.createSubtusk(new SubTask("subtask1",
                "subtask1 description", epic1.getId()));
        taskManager.createSubtusk(new SubTask("subtask2",
                "subtask2 description", epic1.getId()));
        taskManager.createSubtusk(new SubTask("subtask3",
                "subtask3 description", epic1.getId()));
        assertNull(taskManager.clearSubtusksById(2));
    }


    @Test
    void TaskCreatedAndTaskAddedShouldHaveSameVariables() {
        final Task task1 = new Task("task1", "task1 description");
        taskManager.createTask(task1);
        List<Task> list = taskManager.getAllTasks();
        Task task2 = list.getFirst();
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getDescription(), task2.getDescription());
        assertEquals(task1.getStatus(), task2.getStatus());
    }

}