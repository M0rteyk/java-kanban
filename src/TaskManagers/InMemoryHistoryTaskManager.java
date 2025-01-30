package TaskManagers;

import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryTaskManager implements HistoryManager{

    public final List<Task> HistoryStorage = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)){
            return;
        }

        HistoryStorage.add(task.getSnapshot());

        if (HistoryStorage.size() > 10){
            HistoryStorage.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return HistoryStorage;
    }
}
