package task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;


class SubTaskTest {
    @Test
    public void SubtasksWithEqualIdShouldBeEqual() {
        TaskManager manager = new InMemoryTaskManager();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        SubTask subtask1 = new SubTask("subtask1", "description1",startTime, duration, 2);
        subtask1.setStatus(TaskStatus.DONE);
        SubTask subtask2 = manager.updateSubtask(subtask1);
        assertEquals(subtask1, subtask2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }
}