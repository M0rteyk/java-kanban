package taskmanagers;

import java.io.File;

public class Managers {

    /*
     *Приватный конструктор, чтобы не было возможности создавать экземпляр этого класса
     */

    private Managers() {

    }

    /*
     *Возвращаем экземпляр класса InMemoryTaskManager
     */

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultTaskManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }


    /*
     *Возвращаем экземпляр класса InMemoryHistoryTaskManager
     */

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryTaskManager();
    }
}
