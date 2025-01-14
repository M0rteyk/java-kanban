package TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();

    private int genId = 0;

    public Task createTask(Task task){
        if (task.getId() > 0){
            return task;
        }

        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
        return task;

    }

    public Task updateTask(Task task){
        if (task.getId() == 0){
            return task;
        }

        if (!tasks.containsKey(task.getId())){
            return task;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    public ArrayList<Task> printAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    public Task findTaskById(int id){
        return tasks.get(id);
    }

    public void deleteAllTasks(){
        tasks.clear();
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    ////////////////////////////////////////////

    public Epic createEpic(Epic epic){
        if (epic.getId() > 0){
            return epic;
        }

        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic){
        if (epic.getId() == 0){
            return epic;
        }

        if (!epics.containsKey(epic.getId())){
            return epic;
        }


        Epic oldEpic = epics.get(epic.getId());
        ArrayList<SubTask> oldEpicSubtaskList = oldEpic.getSubtuskList();
        if (!oldEpicSubtaskList.isEmpty()){
            for (SubTask subs : oldEpicSubtaskList) {
                subtasks.remove(subs.getId());
            }
        }
        epics.replace(epic.getId(), epic);

        ArrayList<SubTask> newEpicSubtaskList = epic.getSubtuskList();
        if (!newEpicSubtaskList.isEmpty()) {
            for (SubTask subs : newEpicSubtaskList) {
                subtasks.put(subs.getId(), subs);
            }
        }
        updateEpicStatus(epic);
        return epic;
    }

    public ArrayList<Epic> printAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic findEpicById(int id) {
        return epics.get(id);
    }

    public void deleteAllEpic () {
        epics.clear();
        subtasks.clear();
    }

    public void deleteEpicById (int id) {
        ArrayList<SubTask> epicSubtusks = epics.get(id).getSubtuskList();
        epics.remove(id);
        for (SubTask subtusk: epicSubtusks) {
            subtasks.remove(subtusk.getId());
        }
    }

    //////////////////////////////////////////////

    public SubTask createSubtusk(SubTask subTask) {
        if (subTask.getId() > 0) {
            return subTask;
        }

        if (subTask.getEpicId() == 0){
            return subTask;
        }

        if (!epics.containsKey(subTask.getEpicId())){
            return subTask;
        }

        int newId = generateId();
        subTask.setId(newId);
        subtasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubtuskList(subTask);
        epic.getSubtuskIds().add(newId);
        updateEpicStatus(epic);
        return subTask;
    }

    public SubTask updateSubtask(SubTask subTask){
        if (subTask.getId() > 0) {
            return subTask;
        }

        if (subTask.getEpicId() == 0){
            return subTask;
        }

        if (!epics.containsKey(subTask.getEpicId())){
            return subTask;
        }

        SubTask oldSub = subtasks.get(subTask.getId());
        subtasks.replace(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        ArrayList<SubTask> subtuskList = epic.getSubtuskList();
        ArrayList<Integer> subtuskIds = epic.getSubtuskIds();
        subtuskList.remove(oldSub);
        subtuskIds.remove(oldSub.getId());
        epic.setSubtuskList(subtuskList);
        epic.setSubtaskIds(subtuskIds);
        updateEpicStatus(epic);
        return subTask;
    }

    public ArrayList<SubTask> printSubTasks () {
         return new ArrayList<>(subtasks.values());
    }

    public ArrayList<SubTask> printEpicSubtasks(Epic epic){
        return epic.getSubtuskList();
    }

    public ArrayList<Integer> printCountEpicSubtusk(Epic epic){
        return epic.getSubtuskIds();
    }

    public void clearSubtusks(){
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.clearSubtusks();
            epic.setStatus(TaskStatus.NEW);
        }

    }

    public void clearSubtusksById (int id){
        SubTask subTask = subtasks.get(id);
        int epicID = subTask.getEpicId();
        subtasks.remove(id);

        Epic epic = epics.get(epicID);
        ArrayList<SubTask> subtuskList = epic.getSubtuskList();
        ArrayList<Integer> subtuskIds = epic.getSubtuskIds();
        subtuskList.remove(subTask);
        subtuskIds.remove(id);
        epic.setSubtuskList(subtuskList);
        epic.setSubtaskIds(subtuskIds);
        updateEpic(epic);
    }

    //////////////////////////////////////////////////////////

    private void updateEpicStatus(Epic epic){
        int DoneStatusCount = 0;
        int NewStatusCount = 0;
        ArrayList<SubTask> subtasks = epic.getSubtuskList();

        for (SubTask subtask : subtasks){
            if (subtask.getStatus() == TaskStatus.NEW){
                NewStatusCount++;
            }
            if (subtask.getStatus() == TaskStatus.DONE){
                DoneStatusCount++;
            }
        }

        if(NewStatusCount == subtasks.size()){
            epic.setStatus(TaskStatus.NEW);
        } else if (DoneStatusCount == subtasks.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int generateId(){
        return ++genId;
    }
}
