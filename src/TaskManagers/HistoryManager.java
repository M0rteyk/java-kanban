package TaskManagers;

import task.Task;

import java.util.List;

public interface HistoryManager {

    int  MAX_SIZE = 10;

    void addTask(Task task);

    List<Task> getHistory();

}
