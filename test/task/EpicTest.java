package task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import TaskManagers.InMemoryTaskManager;
import TaskManagers.TaskManager;


class EpicTest {

    @Test
    public void EpicsWithEqualIdShouldBeEqual() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic1 = new Epic( "epic1", "description1");
        epic1.setDescription("description2");
        Epic epic2 = manager.updateEpic(epic1);
        assertEquals(epic1, epic2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }
}