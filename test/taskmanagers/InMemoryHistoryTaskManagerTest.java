package taskmanagers;

import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryTaskManager();
    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("Test Task", "Description", testTime, Duration.ofHours(1));
        task.setId(1);

        historyManager.addTask(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Desc", testTime, Duration.ofHours(1));
        task1.setId(1);
        Task task2 = new Task("Task 2", "Desc", testTime.plusHours(1), Duration.ofHours(1));
        task2.setId(2);

        historyManager.addTask(task1);
        historyManager.addTask(task2);

        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }
}