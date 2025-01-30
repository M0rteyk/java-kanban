package task;

import TaskManagers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import TaskManagers.InMemoryTaskManager;

class TaskTest {

    @Test
    public void tasksWithEqualIdShouldBeEqual() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task( "Task1", "description1");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setName("task2");
        Task task2 = manager.updateTask(task1);
        assertEquals(task1, task2,
                "Ошибка! Экземпляры класса Task должны быть равны друг другу, если равен их id;");
    }
}