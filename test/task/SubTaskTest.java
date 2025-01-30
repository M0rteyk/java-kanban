package task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import TaskManagers.InMemoryTaskManager;
import TaskManagers.TaskManager;


class SubTaskTest {
    @Test
    public void SubtasksWithEqualIdShouldBeEqual() {
        TaskManager manager = new InMemoryTaskManager();
        SubTask subtask1 = new SubTask("subtask1", "description1", 2);
        subtask1.setStatus(TaskStatus.DONE);
        SubTask subtask2 = manager.updateSubtask(subtask1);
        assertEquals(subtask1, subtask2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }
}