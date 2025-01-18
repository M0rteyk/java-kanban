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

        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
        return task;

    }

    public Task updateTask(Task task){

        if (!tasks.containsKey(task.getId())){
            return task;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    public ArrayList<Task> getAllTasks(){
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
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic){

        if (!epics.containsKey(epic.getId())){
            return epic;
        }

        epics.replace(epic.getId(), epic);

        updateEpicStatus(epic);
        return epic;
    }

    public ArrayList<Epic> getAllEpics() {
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
        ArrayList<Integer> ids = epics.get(id).getSubtuskIds();
        epics.remove(id);
        for (Integer subid: ids) {
            subtasks.remove(subid);
        }
    }

    //////////////////////////////////////////////

    public SubTask createSubtusk(SubTask subTask) {

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
        epic.getSubtuskIds().add(newId);
        updateEpicStatus(epic);
        return subTask;
    }

    public SubTask updateSubtask(SubTask subTask){

        if (subTask.getEpicId() == 0){
            return subTask;
        }

        if (!epics.containsKey(subTask.getEpicId())){
            return subTask;
        }

        SubTask oldSub = subtasks.get(subTask.getId());
        subtasks.replace(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        ArrayList<Integer> subtuskIds = epic.getSubtuskIds();
        subtuskIds.remove(oldSub.getId());
        epic.setSubtaskIds(subtuskIds);
        updateEpicStatus(epic);
        return subTask;
    }

    public ArrayList<SubTask> getSubTasks () {
         return new ArrayList<>(subtasks.values());
    }

    public ArrayList<SubTask> getEpicSubtasks(Epic epic){
         ArrayList<Integer> ids = epic.getSubtuskIds();
         ArrayList<SubTask> subs = new ArrayList<>();
         for (Integer id : ids){
             subs.add(subtasks.get(id));
         }
        return subs;

    }

    public ArrayList<Integer> getCountEpicSubtusk(Epic epic){
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
        ArrayList<Integer> subtuskIds = epic.getSubtuskIds();
        subtuskIds.remove(id);
        epic.setSubtaskIds(subtuskIds);
        updateEpic(epic);
    }

    //////////////////////////////////////////////////////////

    private void updateEpicStatus(Epic epic){
        int doneStatusCount = 0;
        int newStatusCount = 0;
        ArrayList<Integer> subtask = epic.getSubtuskIds();
        ArrayList<SubTask> subb = new ArrayList<>();

        for (Integer id : subtask) {
            subb.add(subtasks.get(id));
        }

        for (SubTask subTask : subb){
            if (subTask.getStatus() == TaskStatus.NEW){
                newStatusCount++;
            }
            if (subTask.getStatus() == TaskStatus.DONE){
                doneStatusCount++;
            }
        }

        if(newStatusCount == subtask.size()){
            epic.setStatus(TaskStatus.NEW);
        } else if (doneStatusCount == subtask.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int generateId(){
        return ++genId;
    }
}
