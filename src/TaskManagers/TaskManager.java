package TaskManagers;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {

    /*
     *Работа с задачами(Task)
     * Тело методов создания, обновления, поиска и тд.
     */


    public Task createTask(Task task);

    public Task updateTask(Task task);

    public List<Task> getAllTasks();

    public Task findTaskById(int id);

    public void deleteAllTasks();

    public void deleteTaskById(int id);

    /*
     *Работа с эпиками(Epic)
     * Тело методов создания, обновления, поиска и тд.
     */

    public Epic createEpic(Epic epic);

    public Epic updateEpic(Epic epic);

    public List<Epic> getAllEpics();

    public Epic findEpicById(int id);

    public void deleteAllEpic();

    public void deleteEpicById(int id);

    /*
     *Работа с подзадачами(SubTask)
     * Тело методов создания, обновления, поиска и тд.
     */

    public SubTask createSubtusk(SubTask subTask);

    public SubTask updateSubtask(SubTask subTask);

    public SubTask findSubtaskByID(int id);

    public List<SubTask> getSubTasks();

    public List<SubTask> getEpicSubtasks(Epic epic);

    public List<Integer> getCountEpicSubtusk(Epic epic);

    public void clearSubtusks();

    public void clearSubtusksById(int id);

    public List<Task> getHistory();
}


