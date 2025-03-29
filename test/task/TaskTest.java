package task;

import taskmanagers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import taskmanagers.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    @Test
    public void tasksWithEqualIdShouldBeEqual() {
        TaskManager manager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("Task1", "description1", startTime, duration);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setName("task2");
        Task task2 = manager.updateTask(task1);
        assertEquals(task1, task2,
                "Ошибка! Экземпляры класса Task должны быть равны друг другу, если равен их id;");
    }
}