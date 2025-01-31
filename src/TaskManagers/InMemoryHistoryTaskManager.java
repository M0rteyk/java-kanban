package TaskManagers;

import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryTaskManager implements HistoryManager{

    public final List<Task> historyStorage = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)){
            return;
        }

        historyStorage.add(task.getSnapshot());

        if (historyStorage.size() > MAX_SIZE){
            historyStorage.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }
}
