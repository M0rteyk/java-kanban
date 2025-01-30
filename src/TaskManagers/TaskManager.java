package TaskManagers;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    public Task createTask(Task task);

    public Task updateTask(Task task);

    public ArrayList<Task> getAllTasks();

    public Task findTaskById(int id);

    public void deleteAllTasks();

    public Object deleteTaskById(int id);

    ////////////////////////////////////////////

    public Epic createEpic(Epic epic);

    public Epic updateEpic(Epic epic);

    public ArrayList<Epic> getAllEpics();

    public Epic findEpicById(int id);

    public void deleteAllEpic();

    public Object deleteEpicById(int id);

    //////////////////////////////////////////////

    public SubTask createSubtusk(SubTask subTask);

    public SubTask updateSubtask(SubTask subTask);

    public SubTask findSubtaskByID(int id);

    public ArrayList<SubTask> getSubTasks();

    public ArrayList<SubTask> getEpicSubtasks(Epic epic);

    public ArrayList<Integer> getCountEpicSubtusk(Epic epic);

    public void clearSubtusks();

    public Object clearSubtusksById(int id);

    public List<Task> getHistory();
}


